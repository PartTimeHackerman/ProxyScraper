package org.scraper.main.web;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.scraper.main.MainLogger;

import java.io.IOException;

public class ConnectionChecker {
	
	private static ConnectionChecker connectionChecker = new ConnectionChecker();
	
	private ConnectionChecker(){}
	
	public static boolean hasConnection(){
		try {
			Jsoup
					.connect("http://www.google.cat/")
					.userAgent(BrowserVersion.random().ua())
					.ignoreContentType(true)
					.execute();
			
			return true;
		} catch (IOException e) {
			MainLogger.log(ConnectionChecker.class).fatal("No internet connection!");
			return false;
		}
	}
	
	public static String getIp(){
		try {
			Connection.Response response = Jsoup
					.connect("http://tell-my-ip.com/")
					.userAgent(BrowserVersion.random().ua())
					.ignoreContentType(true)
					.execute();
			String ip = response
					.parse()
					.select("#content > div:nth-child(3) > div:nth-child(1) > table:nth-child(1) > tbody > tr:nth-child(1) > td:nth-child(2)")
					.first()
					.text();
			return ip;
		} catch (IOException e) {
			return "";
		}
	}
	
	public static ConnectionChecker get(){
		if(connectionChecker==null) connectionChecker = new ConnectionChecker();
		return connectionChecker;
	}
}
