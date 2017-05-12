package org.scraper.main.checker;

import org.scraper.main.IConcurrent;
import org.scraper.main.Pool;
import org.scraper.main.Proxy;
import org.scraper.main.data.ProxyRepo;

import java.util.List;

public class ProxyCheckerConcurrent extends ProxyChecker implements IConcurrent {
	
	private Pool pool;
	
	public ProxyCheckerConcurrent(Integer timeout, ProxyRepo proxyRepo, Pool pool) {
		super(timeout, proxyRepo);
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
