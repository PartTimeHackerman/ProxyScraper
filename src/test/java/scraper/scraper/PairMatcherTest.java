package scraper.scraper;

import org.junit.Test;
import scraper.Proxy;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PairMatcherTest {
	private final String ip = "111.111.111.111";
	private final String port = "1111";
	private final String ipPort = ip + ":" + port;
	
	private final String ip2 = "222.222.222.222";
	private final String port2 = "2222";
	private final String ipPort2 = ip2 + ":" + port2;
	
	private final IProxyMatcher matcher = new PairMatcher();
	
	@Test
	public void match() throws Exception {
		
		List<Proxy> list;
		String test = ip + "abc:|" + port + "{}def;'" + ip2 + "dfhas!@" + port2;
		
		list = matcher.match(test);
		
		assertTrue(list.size() == 2);
		assertEquals(ipPort, list.get(0).getIpPort());
		assertEquals(ipPort2, list.get(1).getIpPort());
		
	}
	
	@Test
	public void matchReversed() throws Exception {
		
		List<Proxy> list;
		String test = port + "abc:|\n" + ip + "{}def;'" + port2 + "dfhas!@" + ip2;
		
		list = matcher.match(test);
		
		assertTrue(list.size() == 2);
		assertEquals(ipPort, list.get(0).getIpPort());
		assertEquals(ipPort2, list.get(1).getIpPort());
		
	}
	
	@Test
	public void matchOne() throws Exception {
		Proxy proxy = matcher.matchOne(ip + "aowui:ap" + port);
		assertEquals(ipPort, proxy.getIpPort());
		
		proxy = matcher.matchOne(port2 + "aowui:ap" + ip2);
		assertEquals(ipPort2, proxy.getIpPort());
		
	}
	
}