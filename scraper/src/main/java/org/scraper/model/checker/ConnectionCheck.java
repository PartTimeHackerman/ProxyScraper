package org.scraper.model.checker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.scraper.model.Proxy;
import org.scraper.model.web.BrowserVersion;

import java.net.InetSocketAddress;

public abstract class ConnectionCheck extends ConnectionHandler implements IConnectionCheck {
	
	private long time;
	
	private String URL;
	
	private String TITLE;
	
	protected Document response;
	
	protected Proxy.Type type;
	
	ConnectionCheck(Proxy.Type type, String URL, String TITLE) {
		this.type = type;
		this.URL = URL;
		this.TITLE = TITLE;
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
			getResponse(proxy, timeout);
		} catch (Exception e) {
				return super.handleConnection(proxy, timeout);
		}
		if (!titleMatches())
			return super.handleConnection(proxy, timeout);
		
		return this;
	}
	
	private void getResponse(Proxy proxy, Integer timeout) throws Exception {
		java.net.Proxy netProxy = getNetProxy(proxy);
		long connectionTime = System.currentTimeMillis();
		
		response = Jsoup
				.connect(URL)
				.userAgent(BrowserVersion.random().ua())
				.proxy(netProxy)
				.timeout(timeout)
				.execute()
				.parse();
		
		time = System.currentTimeMillis() - connectionTime;
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
	
	private Boolean titleMatches() {
		return TITLE == null || response.title().equals(TITLE);
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
