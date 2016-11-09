package org.scraper.model.checker;

import org.scraper.model.Proxy;

public interface IConnection {
	boolean connect(String url, Integer timeout);
	
	String getTextContent();
	
	Proxy.Anonymity getAnonymity();
	
	long getTime();
	
	Proxy.Type getType();
}
