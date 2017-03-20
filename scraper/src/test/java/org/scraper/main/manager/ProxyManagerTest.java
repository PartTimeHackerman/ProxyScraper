package org.scraper.main.manager;

import org.junit.Before;
import org.junit.Test;
import org.scraper.main.Proxy;
import org.scraper.main.data.ProxyRepo;
import org.scraper.main.limiter.Limiter;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProxyManagerTest {
	
	ProxyRepo proxyRepo;
	Proxy proxy = new Proxy("111.111.111.111:1111");
	Proxy proxy2 = new Proxy("222.222.222.222:2222");
	
	private final Integer limit = 1000;
	
	@Before
	public void setUp(){
		proxyRepo = new ProxyRepo(new Limiter(0));
	}
	
	@Test
	public void addProxy() throws Exception {
		proxyRepo.addProxy(proxy);
		proxyRepo.addProxy(proxy);
		proxyRepo.addProxy(proxy2);
		
		assertTrue(proxyRepo.getAll().size() == 2);
	}
	
	@Test
	public void getIfPresent() throws Exception {
		Proxy http = new Proxy("111.111.111.111:1111");
		http.setType(Proxy.Type.HTTP);
		proxyRepo.addProxy(http);
		assertTrue(proxyRepo.getIfPresent(proxy).getType() == Proxy.Type.HTTP);
	}
	
	@Test
	public void addProxies() throws Exception {
		proxyRepo.addProxies(new ArrayList<>(Arrays.asList(proxy, proxy2)));
		assertTrue(proxyRepo.getAll().size() == 2);
	}
	
	@Test
	public void getChecked() throws Exception {
		proxyRepo.addProxy(proxy);
		proxyRepo.addProxy(proxy2);
		
		Proxy checked = new Proxy("333.333.333.333:3333");
		checked.setChecked(true);
		proxyRepo.addProxy(checked);
		
		assertTrue(proxyRepo.getChecked().size() == 1);
	}
	
	@Test
	public void getWorking() throws Exception {
		proxyRepo.addProxy(proxy);
		proxyRepo.addProxy(proxy2);
		
		Proxy working = new Proxy("333.333.333.333:3333");
		working.setWorking(true);
		proxyRepo.addProxy(working);
		
		assertTrue(proxyRepo.getWorking().size() == 1);
	}
	
}