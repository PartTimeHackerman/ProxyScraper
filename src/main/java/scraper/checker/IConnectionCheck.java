package scraper.checker;

import scraper.Proxy;

public interface IConnectionCheck {
	
	IConnectionCheck connect(Proxy proxy, Integer timeout);
	
	Proxy.Anonymity getAnonymity();
	
	Long getTime();
	
	Proxy.Type getType();
}
