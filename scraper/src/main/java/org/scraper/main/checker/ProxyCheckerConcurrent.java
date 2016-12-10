package org.scraper.main.checker;

import org.scraper.main.IConcurrent;
import org.scraper.main.Proxy;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class ProxyCheckerConcurrent extends ProxyChecker implements IConcurrent {
	
	public ProxyCheckerConcurrent(Integer timeout, List<Proxy> all) {
		super(timeout, all);
	}
	
	@Override
	public List<Proxy> checkProxies(List<Proxy> proxies) {
		List<Callable<Proxy>> calls = proxies.stream()
				.map(proxy ->
							 (Callable<Proxy>) () ->
									 super.checkProxy(proxy))
				.collect(Collectors.toList());
		return sendTasks(calls);
	}
	
	@Override
	public Proxy checkProxy(Proxy proxy) {
		return send(() -> super.checkProxy(proxy));
	}
}
