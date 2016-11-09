package org.scraper.model.checker;

import org.jsoup.Jsoup;
import org.scraper.model.Proxy;
import org.scraper.model.web.BrowserVersion;

import java.io.IOException;
import java.net.InetSocketAddress;

public class JsoupConnection implements IConnection {
	
	private long time;
	
	private org.jsoup.Connection.Response response;
	
	private java.net.Proxy netProxy;
	
	private Proxy proxy;
	
	protected Proxy.Type type;
	
	public JsoupConnection(Proxy.Type type, Proxy proxy){
		this.type = type;
		this.proxy = proxy;
		netProxy = getNetProxy(type);
	}
	
	@Override
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
			if (e.getMessage() != null && e.getMessage().equals("Connection reset"))
				type = Proxy.Type.SOCKS;
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
	
	@Override
	public String getTextContent() {
		try {
			return response.parse().text();
		} catch (IOException e) {
			return "";
		}
	}
	
	
	@Override
	public Proxy.Anonymity getAnonymity() {
			String text = getTextContent();
			String anonymity = "";
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
	
	@Override
	public long getTime() {
		return time;
	}
	
	@Override
	public Proxy.Type getType() {
		return type;
	}
}
