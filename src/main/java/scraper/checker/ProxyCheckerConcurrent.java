package scraper.checker;

import scraper.IConcurrent;
import scraper.Pool;
import scraper.Proxy;
import scraper.data.ProxyRepo;

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
