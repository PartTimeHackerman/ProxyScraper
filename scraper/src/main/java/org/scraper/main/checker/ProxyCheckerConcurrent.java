package org.scraper.main.checker;

import org.scraper.main.IConcurrent;
import org.scraper.main.Pool;
import org.scraper.main.Proxy;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class ProxyCheckerConcurrent extends ProxyChecker implements IConcurrent {
	
	private Pool pool;
	
	public ProxyCheckerConcurrent(Integer timeout, List<Proxy> all, Pool pool) {
		super(timeout, all);
		this.pool = pool;
	}
	
	@Override
	public void checkProxies(List<Proxy> proxies) {
		proxies.forEach(this::checkProxy);
	}
	
	@Override
	public void checkProxy(Proxy proxy) {
		pool.sendTaskOrRunIfFull(()
										 -> super.checkProxy(proxy), false);
	}
}
