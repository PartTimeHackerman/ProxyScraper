package org.scraper.main.checker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.scraper.main.Proxy;
import org.scraper.main.web.BrowserVersion;

import java.io.IOException;
import java.net.InetSocketAddress;

public abstract class ConnectionCheck extends ConnectionHandler implements IConnectionCheck {
	
	private long time;
	
	private String URL;
	
	private String siteTitle;
	
	protected Document response;
	
	protected Proxy.Type type;
	
	ConnectionCheck(Proxy.Type type, String URL) {
		this(type, URL, null);
	}
	
	ConnectionCheck(Proxy.Type type, String URL, String siteTitle) {
		this.type = type;
		this.URL = URL;
		this.siteTitle = siteTitle;
	}
	
	@Override
	public abstract Proxy.Anonymity getAnonymity();
	
	@Override
	public ConnectionCheck handleConnection(Proxy proxy, Integer timeout) {
		return connect(proxy, timeout);
	}
	
	@Override
	public ConnectionCheck connect(Proxy proxy, Integer timeout) {
		try {
			//If type is HTTPS check for HTTPS first, if it throws exception go to HTTP connection succesor. I know, it's fucked up...
			checkHttps(proxy, timeout);
			
			response = getResponse(proxy, timeout);
		} catch (IOException e) {
			return super.handleConnection(proxy, timeout);
		}
		
		if (!titleMatches(response))
			return super.handleConnection(proxy, timeout);
		
		return this;
	}
	
	private void checkHttps(Proxy proxy, Integer timeout) throws IOException {
		if (type == Proxy.Type.HTTPS)
			new ConnectionCheckHttps().getResponse(proxy, timeout);
	}
	
	protected Document getResponse(Proxy proxy, Integer timeout) throws IOException {
		return getResponse(URL, proxy, timeout);
	}
	
	private Document getResponse(String URL, Proxy proxy, Integer timeout) throws IOException {
		java.net.Proxy netProxy = getNetProxy(proxy);
		long connectionTime = System.currentTimeMillis();
		
		Document response = Jsoup
				.connect(URL)
				.userAgent(BrowserVersion.random().ua())
				.proxy(netProxy)
				.timeout(timeout)
				.execute()
				.parse();
		
		time = System.currentTimeMillis() - connectionTime;
		
		return response;
	}
	
	private java.net.Proxy getNetProxy(Proxy proxy) {
		java.net.Proxy.Type prxType = java.net.Proxy.Type.HTTP;
		switch (type) {
			case HTTP:
				prxType = java.net.Proxy.Type.HTTP;
				break;
			case HTTPS:
				prxType = java.net.Proxy.Type.HTTP;
				break;
			case SOCKS:
				prxType = java.net.Proxy.Type.SOCKS;
				break;
		}
		return new java.net.Proxy(prxType, new InetSocketAddress(proxy.getIp(), proxy.getPort()));
	}
	
	private Boolean titleMatches(Document response) {
		return siteTitle == null || response.title().equals(siteTitle);
	}
	
	@Override
	public Long getTime() {
		return time;
	}
	
	@Override
	public Proxy.Type getType() {
		return type;
	}
	
}
