package org.scraper.model.web;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.scraper.model.modles.MainModel;
import org.scraper.model.scrapers.ScrapeType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PHPConnection {

	public static final String URL = "http://absolutelydisgusting.ml/prx.php";

	public static final Gson gson = new Gson();

	public static void main(String... args) {
		//sqlTest();
		
		List<Site> domains = new ArrayList<>();
		domains.add(new Site("test.xyz",ScrapeType.UNCHECKED));
		
		post(domains, PHPMethod.POST_SITES);

		String post = "";
		String get = "";

		List<String> prxs = new ArrayList<>();

		prxs.add("3123123");
		prxs.add("123211");

		List<Site> addrs = new ArrayList<>();

		addrs.add(new Site("asd", ScrapeType.CSS));
		addrs.add(new Site("yjytj", ScrapeType.OCR));

		String json = gson.toJson(addrs);
		String json2 = gson.toJson(prxs);

		Long l = System.currentTimeMillis();

		System.out.println("Parsed: " + (System.currentTimeMillis() - l));
		l = System.currentTimeMillis();

		phpPost(PHPMethod.POST_SITES, json);

		phpPost(PHPMethod.POST_CLICKS, json2);
		System.out.println("Posted: " + (System.currentTimeMillis() - l));
		l = System.currentTimeMillis();

		get = phpGet(PHPMethod.GET_SITES);
		List<Site> addrss = new ArrayList<>(Arrays.asList(new Gson().fromJson(get, Site[].class)));
		System.out.println("ADDRESSES: " + get + "\n");

		get = phpGet(PHPMethod.GET_LINKS);
		System.out.println("LINKS: " + get + "\n");
		List<String> links = new ArrayList<>(Arrays.asList(new Gson().fromJson(get, String[].class)));

		get = phpGet(PHPMethod.GET_CLICKS);
		System.out.println("CLICKS: " + get + "\n");
		List<String> clicks = new ArrayList<>(Arrays.asList(new Gson().fromJson(get, String[].class)));


		System.out.println("Getted: " + (System.currentTimeMillis() - l));
	}

	public static <T> void post(T data, PHPMethod method){
		String json = gson.toJson(data);
		phpPost(method, json);
	}


	private static void phpPost(PHPMethod posting, String json) {
		try {
			Jsoup.connect(URL)
					.timeout(20000)
					.method(Connection.Method.POST)
					.data(posting.cmd(), json)
					.execute();
			//System.out.println(response.parse().text());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String phpGet(PHPMethod method) {
		String parsed = "[]";
		try {
			Connection.Response response = Jsoup.connect(URL)
					.method(Connection.Method.POST)
					.data(method.cmd(), "")
					.execute();
			parsed = response.parse().text();
		} catch (IOException e) {
			MainModel.log.fatal("Can't connect to database!");
		}
		return parsed;
	}


	public static void get(java.util.Collection list, PHPMethod method) {
		list.clear();
		
		switch (method) {
			case GET_SITES:
				list.addAll(new ArrayList<>(Arrays.asList(new Gson().fromJson(phpGet(method), Site[].class))));
				break;
			case GET_DOMAINS:
				list.addAll(new ArrayList<>(Arrays.asList(new Gson().fromJson(phpGet(method), Domain[].class))));
				break;
			default:
				list.addAll(new ArrayList<>(Arrays.asList(new Gson().fromJson(phpGet(method), String[].class))));

		}
	}
	
	/*private static List getList(PHPMethod method, Class clazz){
		return new ArrayList<>(Arrays.asList(new Gson().fromJson(phpGet(method), Site[].class)));
	}*/
	
	
}
