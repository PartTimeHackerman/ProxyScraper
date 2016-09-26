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
	
	public static void main(String... args) {
		ProxyChecker pc = new ProxyChecker(new Pool(100), 3000);
		String socks = "68.67.80.202:41271";
		String elite = "45.79.87.67:8080";
		
		Consumer<Proxy> check = prx -> {
			Proxy result = pc.checkProxy(prx);
			if (result != null) System.out.print(result);
		};
		
		check.accept(new Proxy(socks));
		check.accept(new Proxy(elite));
	}
	
	public ProxyChecker(Pool pool, int timeout) {
		this.pool = pool;
		this.timeout = timeout;
	}
	
	public List<Proxy> checkProxies(List<Proxy> proxies) {
		Main.log.info("Checking list of size {}", proxies.size());
		List<Callable<Proxy>> calls = new ArrayList<>();
		List<Proxy> checked = new ArrayList<>();
		
		proxies.stream()
				.map(proxy ->
							 calls.add(() -> checkProxy(proxy)))
				.collect(Collectors.toList());
		
		try {
			checked = pool.sendTasks(calls);
		} catch (InterruptedException e) {
			Main.log.fatal("Checking interrupted!" + e);
		}
		
		checked.removeIf(Objects::isNull);
		
		return checked;
	}
	
	public Proxy checkProxy(Proxy proxy) {
		
		String ip = proxy.getIp();
		Integer port = proxy.getPort();
		if (port >= 69129) {
			Main.log.warn("Proxy {}:{} port out of range > 69129", ip, port);
			return null;
		}
		
		
		setProxy(proxy);
		if (proxy.isChecked()) {
			Main.log.info("Proxy {}", proxy);
			
		} else {
			Main.log.warn("Proxy {} not working!", proxy.getIpPort());
		}
		
		setChanged();
		notifyObservers(proxy);
		
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
	
	public Proxy checkProxy(String proxy) {
		return checkProxy(new Proxy(proxy));
	}
}
