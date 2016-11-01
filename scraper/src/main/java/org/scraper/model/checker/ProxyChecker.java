package org.scraper.model.checker;

import org.scraper.model.*;
import org.scraper.model.Proxy;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProxyChecker extends Observable {
	
	private Pool pool;
	
	private int timeout;
	
	private List<Proxy> all;
	
	public static void main(String... args) {
	}
	
	public ProxyChecker(Pool pool, int timeout, List<Proxy> all) {
		this.pool = pool;
		this.timeout = timeout;
		this.all = all;
	}
	
	public List<Proxy> checkProxies(List<Proxy> proxies, boolean wait) {
		Main.log.info("Checking list of size {}", proxies.size());
		List<Callable<Proxy>> calls = new ArrayList<>();
		List<Proxy> checked;
		
		proxies.stream()
				.map(proxy ->
							 calls.add(() -> checkProxy(proxy)))
				.collect(Collectors.toList());
		
		checked = pool.sendTasks(calls);
		
		checked.removeIf(Objects::isNull);
		
		return checked;
	}
	
	public Proxy checkProxyConcurrent(Proxy proxy, boolean wait) {
		pool.sendTask(() -> checkProxy(proxy), wait);
		return proxy;
	}
	
	public Proxy checkProxy(Proxy proxy) {
		
		if (all.contains(proxy)) {
			Proxy doubled = all.get(all.indexOf(proxy));
			if (doubled.isChecked()) {
				Main.log.info("Proxy double {}", doubled);
				return doubled;
			}
		}
		
		String ip = proxy.getIp();
		Integer port = proxy.getPort();
		if (port >= 69129) {
			Main.log.warn("Proxy {}:{} port out of range > 69129", ip, port);
			return proxy;
		}
		setProxy(proxy);
		if (proxy.isWorking()) {
			Main.log.info("Proxy {}", proxy);
			
		} else {
			Main.log.warn("Proxy {} not working!", proxy.getIpPort());
		}
		
		proxy.setChecked(true);
		return proxy;
	}
	
	private void setProxy(Proxy proxy) {
		String httpsUrl = "https://www.google.cat/";
		String ping = "http://absolutelydisgusting.ml/ping.php";
		//ping = "http://www.google.com/";
		
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
