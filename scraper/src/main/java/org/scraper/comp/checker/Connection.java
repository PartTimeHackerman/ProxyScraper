package org.scraper.comp.checker;

import org.jsoup.Jsoup;
import org.scraper.comp.Proxy;
import org.scraper.comp.web.BrowserVersion;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Connection {
	
	private long time;
	
	private org.jsoup.Connection.Response response;
	
	private java.net.Proxy netProxy;
	
	private Proxy proxy;
	
	protected Proxy.Anonymity anonymity;
	
	protected Proxy.Type type;
	
	public Connection(Proxy.Type type, Proxy proxy){
		this.proxy = proxy;
		netProxy = getNetProxy(type);
	}
	
	public boolean connect(String url, Integer timeout) {
		try {
			long connectionTime = System.currentTimeMillis();
			response = Jsoup
					.connect(url)
					.userAgent(BrowserVersion.random().ua())
					.proxy(netProxy)
					.ignoreContentType(true)
					.timeout(timeout)
					.execute();
			
			String title = response.parse().title();
			if (!title.equals("Letters") && !title.equals("Google")) return false;
			
			time = System.currentTimeMillis() - connectionTime;
			return true;
			
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().equals("Connection reset")) type = Proxy.Type.SOCKS;
			//Main.log.fatal("type {}, url {}, ip {}, exception {}", proxy.type(), url, proxy.address(), e.getMessage() != null ? e.getMessage() : "");
			return false;
		}
	}
	
	private java.net.Proxy getNetProxy(Proxy.Type type){
		java.net.Proxy.Type prxType = java.net.Proxy.Type.HTTP;
		switch (type){
			case HTTP: prxType = java.net.Proxy.Type.HTTP; break;
			case HTTPS: prxType = java.net.Proxy.Type.HTTP; break;
			case SOCKS: prxType = java.net.Proxy.Type.SOCKS; break;
		}
		return new java.net.Proxy(prxType, new InetSocketAddress(proxy.getIp(), proxy.getPort()));
	}
	
	public String getTextContent() {
		try {
			return response.parse().text();
		} catch (IOException e) {
			return "";
		}
	}
	
	
	public Proxy.Anonymity getAnonymity() {
		if (anonymity == null) {
			String text = getTextContent();
			String anonymity ="";
			if(text.contains("|"))
			 anonymity = text.split("\\|")[1];
			switch (anonymity) {
				case "e": {
					return Proxy.Anonymity.ELITE;
				}
				case "a": {
					return Proxy.Anonymity.ANONYMOUS;
				}
				case "t": {
					return Proxy.Anonymity.TRANSPARENT;
				}
				default: {
					return Proxy.Anonymity.TRANSPARENT;
				}
			}
		}
		return anonymity;
	}
	
	public long getTime() {
		return time;
	}
	
	public Proxy.Type getType() {
		return type;
	}
}
