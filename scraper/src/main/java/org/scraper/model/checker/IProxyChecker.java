package org.scraper.model.checker;

import org.scraper.model.Proxy;

import java.util.List;

public interface IProxyChecker {
	
	List<Proxy> checkProxies(List<Proxy> proxies);
	
	Proxy checkProxy(Proxy proxy);
}
