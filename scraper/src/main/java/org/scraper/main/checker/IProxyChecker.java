package org.scraper.main.checker;

import org.scraper.main.Proxy;

import java.util.List;

public interface IProxyChecker {
	
	void checkProxies(List<Proxy> proxies);
	
	void checkProxy(Proxy proxy);
}
