package org.scraper.comp.web;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Proxy {

	public static List<String> proxy;

	private static String proxyPath = System.getProperty("user.dir")+"/prx.txt";

	private static Integer prxCount = 0;

	private static AtomicInteger testCount = new AtomicInteger(0);


	private static void dlProxy(){
		proxy = new ArrayList<>();

		ExecutorService executor = Executors.newFixedThreadPool(100, r -> {
			Thread t = Executors.defaultThreadFactory().newThread(r);
			t.setDaemon(true);
			return t;
		});

		try (Stream<String> stream = Files.lines(Paths.get(proxyPath))) {
			stream.forEach(p -> executor.execute(()->{
				if(proxyAlive(p)) {proxy.add(p); System.out.println(testCount.addAndGet(1) + ": " + p + " aliveeeeeeeeeeeeeeeeeeeeeeeeeeeee");}
				else{System.out.println(testCount.addAndGet(1) + ": " + p + " DEAD!!! >:D");}
			}));
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdown();
	}

	public static List<String> getProxy(){
		if(proxy==null) dlProxy();
		return proxy;
	}

	public static String getNextWorking(){
		if(proxy==null) dlProxy();
		String aProxy = proxy.get(prxCount++);

		return proxyAlive(aProxy)? aProxy : getNextWorking();
	}

	public static boolean proxyAlive(String proxy){
		try {
			//Connection.Response res = Jsoup.connect("http://www.wykop.pl").proxy(proxy.split(":")[0], Integer.parseInt(proxy.split(":")[1])).timeout(3000).execute();
			Document doc = Jsoup.connect("http://www.google.com").proxy(proxy.split(":")[0], Integer.parseInt(proxy.split(":")[1])).timeout(60000).get();
			String title = doc.title();
			if(!title.equals("Google")) return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
