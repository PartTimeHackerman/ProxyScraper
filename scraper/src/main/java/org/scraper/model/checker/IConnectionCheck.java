package org.scraper.model.checker;

import org.scraper.model.Proxy;

public interface IConnectionCheck {
	
	IConnectionCheck connect(Proxy proxy, Integer timeout);
	
	Proxy.Anonymity getAnonymity();
	
	Long getTime();
	
	Proxy.Type getType();
}
