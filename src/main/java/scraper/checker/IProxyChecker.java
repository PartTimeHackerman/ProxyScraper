package scraper.checker;

import scraper.Proxy;

import java.util.List;

public interface IProxyChecker {
	
	void checkProxies(List<Proxy> proxies);
	
	void checkProxy(Proxy proxy);
}
