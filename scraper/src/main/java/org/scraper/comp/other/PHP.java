package org.scraper.comp.other;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.scraper.comp.scraper.Address;
import org.scraper.comp.scraper.PostGetPHP;
import org.scraper.comp.web.Browser;
import org.scraper.comp.web.BrowserVersion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PHP {

	public static final String URL = "http://absolutelydisgusting.ml/prx.php";

	public static final Gson gson = new Gson();

	public static void main(String... args) {

		String post = "";
		String get = "";

		List<String> prxs = new ArrayList<>();

		prxs.add("3123123");
		prxs.add("123211");

		List<Address> addrs = new ArrayList<>();

		addrs.add(new Address("asd", Address.Type.BEFORE_AFTER, "ss"));
		addrs.add(new Address("yjytj", Address.Type.OCR, "adasd"));

		String json = gson.toJson(addrs);
		String json2 = gson.toJson(prxs);

		Long l = System.currentTimeMillis();

		System.out.println("Parsed: " + (System.currentTimeMillis() - l));
		l = System.currentTimeMillis();

		phpPost(PostGetPHP.POST_ADDRS, json);
		phpPost(PostGetPHP.POST_CLICKS, json2);
		System.out.println("Posted: " + (System.currentTimeMillis() - l));
		l = System.currentTimeMillis();

		get = phpGet(PostGetPHP.GET_ADDRS);
		List<Address> addrss = new ArrayList<>(Arrays.asList(new Gson().fromJson(get, Address[].class)));
		System.out.println("ADDRESSES: " + get + "\n");

		get = phpGet(PostGetPHP.GET_LINKS);
		System.out.println("LINKS: " + get + "\n");
		List<String> links = new ArrayList<>(Arrays.asList(new Gson().fromJson(get, String[].class)));

		get = phpGet(PostGetPHP.GET_CLICKS);
		System.out.println("CLICKS: " + get + "\n");
		List<String> clicks = new ArrayList<>(Arrays.asList(new Gson().fromJson(get, String[].class)));


		System.out.println("Getted: " + (System.currentTimeMillis() - l));
	}

	public static void phpPost(PostGetPHP posting, String json) {
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

	public static String phpGet(PostGetPHP getting) {
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


	public static Object get(PostGetPHP method) {
		switch (method) {
			case GET_ADDRS:
				return new ArrayList<>(Arrays.asList(new Gson().fromJson(phpGet(method), Address[].class)));
			default:
				return new ArrayList<>(Arrays.asList(new Gson().fromJson(phpGet(method), String[].class)));

		}
	}


	private static void click() {
		WebDriver driver = Browser.getBrowser(null, BrowserVersion.FIREFOX_WIN, "p");
		driver.get("http://tell-my-ip.com/");
		((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		System.out.println(driver.toString());

		String proxyip = "40.85.180.58", proxyport = "3128";
		String proxy = "40.85.180.58:3128";
		org.openqa.selenium.Proxy browserProxy = new org.openqa.selenium.Proxy();
		browserProxy
				.setHttpProxy(proxy)
				.setSslProxy(proxy)
				.setFtpProxy(proxy)
				.setSocksProxy(proxy);

		String js = "var page=this;" +
				"phantom.setProxy(\"" + proxyip + "\"," + proxyport + ");";//, http, "", ""

		((PhantomJSDriver) driver).executePhantomJS(js);

		driver.get("http://tell-my-ip.com/");
		((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	}
}
