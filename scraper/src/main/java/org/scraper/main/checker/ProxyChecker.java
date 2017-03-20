package org.scraper.main.checker;

import org.scraper.main.MainLogger;
import org.scraper.main.Proxy;
import org.scraper.main.limiter.Switchable;

import java.util.List;
import java.util.Observable;

public class ProxyChecker extends Observable implements IProxyChecker, Switchable {
	
	private Integer timeout = 30000;
	
	private List<Proxy> all;
	
	private Boolean on = true;
	
	public ProxyChecker(Integer timeout, List<Proxy> all) {
		this.timeout = timeout;
		this.all = all;
	}
	
	@Override
	public void checkProxies(List<Proxy> proxies) {
		MainLogger.log().info("Checking list of size {}", proxies.size());
		
		proxies.forEach(this::checkProxy);
	}
	
	@Override
	public void checkProxy(Proxy proxy) {
		if (!isOn())
			return;
		
		if (all.contains(proxy)) {
			Proxy doubled = all.get(all.indexOf(proxy));
			if (doubled.isChecked()) {
				MainLogger.log().info("Proxy doubled {}", doubled);
				return;
			}
		}
		
		String ip = proxy.getIp();
		Integer port = proxy.getPort();
		if (port >= 69129) {
			MainLogger.log().warn("Proxy {}:{} port out of range > 69129", ip, port);
			return;
		}
		
		setProxy(proxy);
		if (proxy.isWorking()) {
			MainLogger.log().info("Proxy {}", proxy);
		} else {
			MainLogger.log().warn("Proxy {} not working!", proxy.getIpPort());
		}
		
		proxy.setChecked(true);
	}
	
	private void setProxy(Proxy proxy) {
		IConnectionCheckers handlers = new ConnectionCheckersExternal();
		proxy.setUpProxy(handlers.check(proxy, timeout));
	}
	
	@Override
	public void turnOn() {
		on = true;
	}
	
	@Override
	public void turnOff() {
		on = false;
	}
	
	@Override
	public Boolean isOn() {
		return on;
	}
}
