package org.scraper.model.checker;

import org.scraper.model.Proxy;
import org.scraper.model.modles.MainModel;

import java.util.*;

public class ProxyChecker extends Observable implements IProxyChecker {
	
	private Integer timeout = 30000;
	
	private List<Proxy> all;
	
	public ProxyChecker(Integer timeout, List<Proxy> all) {
		this.timeout = timeout;
		this.all = all;
	}
	
	@Override
	public List<Proxy> checkProxies(List<Proxy> proxies) {
		MainModel.log.info("Checking list of size {}", proxies.size());
		
		proxies.forEach(this::checkProxy);
		
		return proxies;
	}
	
	@Override
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
		String httpsUrl = "https://www.google.cat/";
		String ping = "http://absolutelydisgusting.ml/ping.php";
		
		IConnection https = new JsoupConnection(Proxy.Type.HTTPS, proxy);
		boolean connected = https.connect(httpsUrl, timeout);
		
		if (connected) {
			proxy.setUpProxy(https);
		} else if (https.getType() == Proxy.Type.SOCKS) {
			IConnection socks = new JsoupConnection(Proxy.Type.SOCKS, proxy);
			if (socks.connect(ping, timeout))
				proxy.setUpProxy(socks);
			connected = true;
		}
		
		if (!connected) {
			IConnection http = new JsoupConnection(Proxy.Type.HTTP, proxy);
			if (http.connect(ping, timeout)) {
				proxy.setUpProxy(http);
			}
		}
	}
}
