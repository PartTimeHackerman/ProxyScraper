package org.scraper.comp.web;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.scraper.comp.Main;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class PHP {

	public static final String URL = "http://absolutelydisgusting.ml/prx.php";

	public static final Gson gson = new Gson();

	public static void main(String... args) {
		//sqlTest();

		String post = "";
		String get = "";

		List<String> prxs = new ArrayList<>();

		prxs.add("3123123");
		prxs.add("123211");

		List<Address> addrs = new ArrayList<>();

		addrs.add(new Address("asd", Address.Type.CSS, "ss"));
		addrs.add(new Address("yjytj", Address.Type.OCR, "adasd"));

		String json = gson.toJson(addrs);
		String json2 = gson.toJson(prxs);

		Long l = System.currentTimeMillis();

		System.out.println("Parsed: " + (System.currentTimeMillis() - l));
		l = System.currentTimeMillis();

		phpPost(PHPMethod.POST_ADDRS, json);

		phpPost(PHPMethod.POST_CLICKS, json2);
		System.out.println("Posted: " + (System.currentTimeMillis() - l));
		l = System.currentTimeMillis();

		get = phpGet(PHPMethod.GET_ADDRS);
		List<Address> addrss = new ArrayList<>(Arrays.asList(new Gson().fromJson(get, Address[].class)));
		System.out.println("ADDRESSES: " + get + "\n");

		get = phpGet(PHPMethod.GET_LINKS);
		System.out.println("LINKS: " + get + "\n");
		List<String> links = new ArrayList<>(Arrays.asList(new Gson().fromJson(get, String[].class)));

		get = phpGet(PHPMethod.GET_CLICKS);
		System.out.println("CLICKS: " + get + "\n");
		List<String> clicks = new ArrayList<>(Arrays.asList(new Gson().fromJson(get, String[].class)));


		System.out.println("Getted: " + (System.currentTimeMillis() - l));
	}

	public static <T> void post(T addresses, PHPMethod method){
		String json = gson.toJson(addresses);
		phpPost(method, json);
	}

	public static void postClicks(List<String> clicks){
		String json = gson.toJson(clicks);
		phpPost(PHPMethod.POST_ADDRS, json);
	}

	public static void phpPost(PHPMethod posting, String json) {
		try {
			Connection.Response response = Jsoup.connect(URL)
					.timeout(20000)
					.method(Connection.Method.POST)
					.data(posting.cmd(), json)
					.execute();
			System.out.println(response.parse().text());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String phpGet(PHPMethod getting) {
		String getted = "";
		try {
			Connection.Response response = Jsoup.connect(URL)
					.method(Connection.Method.POST)
					.data(getting.cmd(), "")
					.execute();
			//System.out.println(response.parse().text());
			getted = response.parse().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//List<Address> addrss = new ArrayList<>(Arrays.asList(new Gson().fromJson(getted, Address[].class)));
		return getted;
	}


	public static <T> void get(java.util.Collection<T> list, PHPMethod method) {
		list.clear();
		switch (method) {
			case GET_ADDRS:
				list.addAll(new ArrayList<>(Arrays.asList((T[]) new Gson().fromJson(phpGet(method), Address[].class))));
				break;
			default:
				list.addAll(new ArrayList<>(Arrays.asList((T[]) new Gson().fromJson(phpGet(method), String[].class))));

		}
	}

	public static void sqlTest(){
		java.sql.Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			Properties connProp = new Properties();
			connProp.setProperty("user", "u501421373_addrs");
			connProp.setProperty("password", "tjbmh!");
			connProp.setProperty("connectTimeout", "10000");
			connProp.setProperty("socketTimeout", "10000");

			connection = DriverManager.getConnection("jdbc:mysql://1freehosting.com:3306/u501421373_addrs",connProp);

			Statement statement = connection.createStatement();

			String query = "SELECT proxy FROM Clicks WHERE clicks<5";

			ResultSet result = statement.executeQuery(query);

			while (result.next()){
				Main.log.info(result.getString("proxy"));
			}

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
