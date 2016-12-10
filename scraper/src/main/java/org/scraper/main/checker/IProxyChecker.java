package org.scraper.main.checker;

import org.scraper.main.Proxy;

import java.util.List;

public interface IProxyChecker {
	
	List<Proxy> checkProxies(List<Proxy> proxies);
	
	Proxy checkProxy(Proxy proxy);
}
