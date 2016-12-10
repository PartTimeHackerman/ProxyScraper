package org.scraper.main.checker;

import org.scraper.main.Proxy;

public interface IConnectionCheck {
	
	IConnectionCheck connect(Proxy proxy, Integer timeout);
	
	Proxy.Anonymity getAnonymity();
	
	Long getTime();
	
	Proxy.Type getType();
}
