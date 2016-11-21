package org.scraper.model.managers;

import org.junit.Before;
import org.junit.Test;
import org.scraper.model.Proxy;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProxyManagerTest {
	
	ProxyManager proxyManager;
	Proxy proxy = new Proxy("111.111.111.111:1111");
	Proxy proxy2 = new Proxy("222.222.222.222:2222");
	
	private final Integer limit = 1000;
	
	@Before
	public void setUp(){
		proxyManager = new ProxyManager(limit);
	}
	
	@Test
	public void addProxy() throws Exception {
		proxyManager.addProxy(proxy);
		proxyManager.addProxy(proxy);
		proxyManager.addProxy(proxy2);
		
		assertTrue(proxyManager.getAll().size() == 2);
	}
	
	@Test
	public void getIfPresent() throws Exception {
		Proxy http = new Proxy("111.111.111.111:1111");
		http.setType(Proxy.Type.HTTP);
		proxyManager.addProxy(http);
		assertTrue(proxyManager.getIfPresent(proxy).getType() == Proxy.Type.HTTP);
	}
	
	@Test
	public void addProxies() throws Exception {
		proxyManager.addProxies(new ArrayList<>(Arrays.asList(proxy, proxy2)));
		assertTrue(proxyManager.getAll().size() == 2);
	}
	
	@Test
	public void getLimit() throws Exception {
		assertEquals(limit, proxyManager.getLimit());
	}
	
	@Test
	public void getChecked() throws Exception {
		proxyManager.addProxy(proxy);
		proxyManager.addProxy(proxy2);
		
		Proxy checked = new Proxy("333.333.333.333:3333");
		checked.setChecked(true);
		proxyManager.addProxy(checked);
		
		assertTrue(proxyManager.getChecked().size() == 1);
	}
	
	@Test
	public void getWorking() throws Exception {
		proxyManager.addProxy(proxy);
		proxyManager.addProxy(proxy2);
		
		Proxy working = new Proxy("333.333.333.333:3333");
		working.setWorking(true);
		proxyManager.addProxy(working);
		
		assertTrue(proxyManager.getWorking().size() == 1);
	}
	
}