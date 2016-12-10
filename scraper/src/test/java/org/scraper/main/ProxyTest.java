package org.scraper.main;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProxyTest {
	
	private Proxy proxy;
	
	private final String ip = "111.111.111.111";
	private final String secondIP = "222.222.222.222";
	
	private final Integer port = 8080;
	private final Integer secondPort = 1234;
	
	private final String ipPort = ip + ":" + port;
	
	
	
	@Before
	public void setUp(){
		proxy = new Proxy(ip, port);
	}
	
	@Test
	public void constructorTest(){
		proxy = new Proxy(ip, port);
		assertEquals(ipPort, proxy.getIpPort());
		proxy = new Proxy(ipPort);
		assertEquals(ipPort, proxy.getIpPort());
	}
	
	@Test
	public void setUpProxy() throws Exception {
		//TODO
		/*Connection connection = new Connection(Proxy.Type.HTTP, proxy);
		proxy.setUpProxy(connection);
		assertEquals(Proxy.Type.HTTP, proxy.getType());*/
	}
	
	@Test
	public void getSetIp() throws Exception {
		proxy.setIp(secondIP);
		assertEquals(secondIP, proxy.getIp());
		
	}
	
	@Test
	public void getSetPort() throws Exception {
		proxy.setPort(secondPort);
		assertEquals(secondPort, proxy.getPort());
	}
	
	@Test
	public void getSetIpPort() throws Exception {
		String ipPort = secondIP + secondPort;
		proxy.setIpPort(ipPort);
		assertEquals(ipPort, proxy.getIpPort());
	}
	
	@Test
	public void getSetType() throws Exception {
		proxy.setType(Proxy.Type.HTTPS);
		assertEquals(Proxy.Type.HTTPS, proxy.getType());
	}
	
	@Test
	public void getTypeString() throws Exception {
		proxy.setType(Proxy.Type.HTTPS);
		assertEquals("HTTPS", proxy.getTypeString());
	}
	
	
	@Test
	public void getSetAnonymity() throws Exception {
		proxy.setAnonymity(Proxy.Anonymity.ANONYMOUS);
		assertEquals(Proxy.Anonymity.ANONYMOUS, proxy.getAnonymity());
	}
	
	@Test
	public void getAnonymityString() throws Exception {
		proxy.setAnonymity(Proxy.Anonymity.ANONYMOUS);
		assertEquals("ANONYMOUS", proxy.getAnonymityString());
	}
	
	@Test
	public void getSetSpeed() throws Exception {
		Long speed = 12000L;
		proxy.setSpeed(speed);
		assertEquals(speed, proxy.getSpeed());
	}
	
	@Test
	public void isSetChecked() throws Exception {
		proxy.setChecked(true);
		assertEquals(true, proxy.isChecked());
	}
	
	@Test
	public void isWorking() throws Exception {
		proxy.setWorking(true);
		assertEquals(true, proxy.isWorking());
	}
	
}