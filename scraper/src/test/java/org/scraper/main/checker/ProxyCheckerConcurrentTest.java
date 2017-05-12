package org.scraper.main.checker;

import org.junit.Test;
import org.scraper.main.Pool;
import org.scraper.main.Proxy;
import org.scraper.main.TestsUtils;
import org.scraper.main.data.ProxyRepo;
import org.scraper.main.limiter.Limiter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ProxyCheckerConcurrentTest {
	
	private final IProxyChecker checker = new ProxyCheckerConcurrent(3000, new ProxyRepo(new Limiter(1)), new Pool());
	
	@Test
	public void checkProxies() throws Exception {
		List<Proxy> proxies = new ArrayList<>();
		proxies.add(TestsUtils.brokenProxy1);
		proxies.add(TestsUtils.localProxy);
		proxies.add(TestsUtils.brokenProxy2);
		proxies.add(TestsUtils.brokenProxy3);
		
		checker.checkProxies(proxies);
		
		
		proxies.forEach(proxy -> assertTrue(proxy.isChecked()));
	}
	
}