package scraper.checker;

import scraper.MainLogger;
import scraper.Proxy;
import scraper.data.ProxyRepo;
import scraper.limiter.Switchable;

import java.util.List;
import java.util.Observable;
import java.util.Vector;

public class ProxyChecker extends Observable implements IProxyChecker, Switchable {
	
	private static Vector<Proxy> checkedProxies = new Vector<>();
	
	private Integer timeout = 30000;
	
	private Boolean on = true;
	
	private ProxyRepo proxyRepo;
	
	public ProxyChecker(Integer timeout, ProxyRepo proxyRepo) {
		this.timeout = timeout;
		this.proxyRepo = proxyRepo;
	}
	
	@Override
	public void checkProxies(List<Proxy> proxies) {
		MainLogger.log(this).info("Checking list of size {}", proxies.size());
		
		proxies.forEach(this::checkProxy);
	}
	
	@Override
	public void checkProxy(Proxy proxy) {
		
		if (!isOn() || checkedProxies.contains(proxy))
			return;
		
		proxy.setChecked(true);
		checkedProxies.add(proxy);
		
		String ip = proxy.getIp();
		Integer port = proxy.getPort();
		if (port >= 69129) {
			MainLogger.log(this).warn("Proxy {}:{} port out of range > 69129", ip, port);
			return;
		}
		
		check(proxy);
		if (proxy.isWorking()) {
			MainLogger.log(this).info("Proxy {}", proxy);
			proxyRepo.addProxy(proxy);
		} else {
			MainLogger.log(this).warn("Proxy {} not working!", proxy.getIpPort());
		}
		
	}
	
	private void check(Proxy proxy) {
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
