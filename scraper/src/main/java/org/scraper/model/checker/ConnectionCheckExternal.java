package org.scraper.model.checker;

import org.scraper.model.Proxy;


public class ConnectionCheckExternal extends ConnectionCheck {
	
	private static final String URL = "http://tell-my-ip.com/";
	private static final String TITLE = "Get my IP address. Reveal my IP Address. Locate position on map";
	
	ConnectionCheckExternal(Proxy.Type type) {
		super(type, URL, TITLE);
	}
	
	@Override
	public Proxy.Anonymity getAnonymity() {
		String anonymity = response.select("#content > div:nth-child(3) > div:nth-child(1) > table:nth-child(1) > tbody > tr:nth-child(9) > td:nth-child(2)").get(0).text();
		switch (anonymity) {
			case "Elite (or no proxy)": {
				return Proxy.Anonymity.ELITE;
			}
			case "Anonymous": {
				return Proxy.Anonymity.ANONYMOUS;
			}
			case "Transparent": {
				return Proxy.Anonymity.TRANSPARENT;
			}
			default: {
				return Proxy.Anonymity.ANONYMOUS;
			}
		}
	}
}
