package org.scraper.model.checker;

import org.junit.Test;
import org.scraper.model.Proxy;
import org.scraper.model.TestsUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ProxyCheckerConcurrentTest {
	
	private final IProxyChecker checker = new ProxyCheckerConcurrent(3000, new ArrayList<>());
	
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