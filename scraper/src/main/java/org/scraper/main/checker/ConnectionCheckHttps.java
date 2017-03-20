package org.scraper.main.checker;

import org.scraper.main.Proxy;

public class ConnectionCheckHttps extends ConnectionCheck{
	
	private static final String URL = "https://www.google.cat/";
	private static final String TITLE = "Google";
	
	ConnectionCheckHttps() {
		super(Proxy.Type.HTTPS, URL, TITLE);
	}
	
	@Override
	public Proxy.Anonymity getAnonymity() {
		return Proxy.Anonymity.ANONYMOUS;
	}
}
