package scraper.web;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import scraper.MainLogger;
import scraper.data.Domain;
import scraper.data.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class PHPConnection {

	public static final String URL = "http://lifechangertoworse.ga/prx.php";

	public static final Gson gson = new Gson();

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
			MainLogger.log(PHPConnection.class).fatal("Can't connect to database!");
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
