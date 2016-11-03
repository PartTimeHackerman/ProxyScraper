package org.scraper.model.checker;

import org.scraper.model.*;
import org.scraper.model.Proxy;
import org.scraper.model.modles.MainModel;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class ProxyChecker extends Observable {
	
	private Pool pool;
	
	private int timeout;
	
	private List<Proxy> all;
	
	private final String httpsUrl = "https://www.google.cat/";
	private final String ping = "http://absolutelydisgusting.ml/ping.php";
	
	public ProxyChecker(Pool pool, int timeout, List<Proxy> all) {
		this.pool = pool;
		this.timeout = timeout;
		this.all = all;
	}
	
	public List<Proxy> checkProxies(List<Proxy> proxies) {
		MainModel.log.info("Checking list of size {}", proxies.size());
		List<Callable<Proxy>> calls = new ArrayList<>();
		
		proxies.stream()
				.map(proxy ->
							 calls.add(() -> checkProxy(proxy)))
				.collect(Collectors.toList());
		
		return pool.sendTasks(calls);
	}
	
	public Proxy checkProxyConcurrent(Proxy proxy, boolean wait) {
		pool.sendTask(() -> checkProxy(proxy), wait);
		return proxy;
	}
	
	public Proxy checkProxy(Proxy proxy) {
		
		if (all.contains(proxy)) {
			Proxy doubled = all.get(all.indexOf(proxy));
			if (doubled.isChecked()) {
				MainModel.log.info("Proxy double {}", doubled);
				return doubled;
			}
		}
		
		String ip = proxy.getIp();
		Integer port = proxy.getPort();
		if (port >= 69129) {
			MainModel.log.warn("Proxy {}:{} port out of range > 69129", ip, port);
			return proxy;
		}
		
		setProxy(proxy);
		if (proxy.isWorking()) {
			MainModel.log.info("Proxy {}", proxy);
		} else {
			MainModel.log.warn("Proxy {} not working!", proxy.getIpPort());
		}
		
		proxy.setChecked(true);
		return proxy;
	}
	
	
	private void setProxy(Proxy proxy) {
		Connection https = new Connection(Proxy.Type.HTTPS, proxy);
		
		boolean connected = https.connect(httpsUrl, timeout);
		
		if (connected) {
			proxy.setProxy(https);
		} else if (https.getType() == Proxy.Type.SOCKS) {
			Connection socks = new Connection(Proxy.Type.SOCKS, proxy);
			if (socks.connect(ping, timeout))
				proxy.setProxy(socks);
			connected = true;
		}
		
		if (!connected) {
			Connection http = new Connection(Proxy.Type.HTTP, proxy);
			if (http.connect(ping, timeout)) {
				proxy.setProxy(http);
			}
		}
	}
	
	public Proxy checkProxyConcurrent(String proxy) {
		return checkProxyConcurrent(new Proxy(proxy), false);
	}
}
