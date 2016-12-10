package org.scraper.main.checker;

import org.scraper.main.MainLogger;
import org.scraper.main.Proxy;

import java.util.List;
import java.util.Observable;

public class ProxyChecker extends Observable implements IProxyChecker {
	
	private Integer timeout = 30000;
	
	private List<Proxy> all;
	
	public ProxyChecker(Integer timeout, List<Proxy> all) {
		this.timeout = timeout;
		this.all = all;
	}
	
	@Override
	public List<Proxy> checkProxies(List<Proxy> proxies) {
		MainLogger.log().info("Checking list of size {}", proxies.size());
		
		proxies.forEach(this::checkProxy);
		
		return proxies;
	}
	
	@Override
	public Proxy checkProxy(Proxy proxy) {
		if (all.contains(proxy)) {
			Proxy doubled = all.get(all.indexOf(proxy));
			if (doubled.isChecked()) {
				MainLogger.log().info("Proxy double {}", doubled);
				return doubled;
			}
		}
		
		String ip = proxy.getIp();
		Integer port = proxy.getPort();
		if (port >= 69129) {
			MainLogger.log().warn("Proxy {}:{} port out of range > 69129", ip, port);
			return proxy;
		}
		
		setProxy(proxy);
		if (proxy.isWorking()) {
			MainLogger.log().info("Proxy {}", proxy);
		} else {
			MainLogger.log().warn("Proxy {} not working!", proxy.getIpPort());
		}
		
		proxy.setChecked(true);
		return proxy;
	}
	
	
	private void setProxy(Proxy proxy) {
		IConnectionCheckers handlers = new ConnectionCheckersScript();
		proxy.setUpProxy(handlers.check(proxy, timeout));
	}
}
