package org.scraper.main;

import org.scraper.main.checker.IProxyChecker;
import org.scraper.main.data.ProxyRepo;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProxiesObserver implements Observer {
	
	private ProxyRepo proxyRepo;
	
	private IProxyChecker proxyChceker;
	
	private AtomicBoolean checkOnFly;
	
	public ProxiesObserver(ProxyRepo proxyRepo, IProxyChecker proxyChceker, AtomicBoolean checkOnFly) {
		this.proxyRepo = proxyRepo;
		this.proxyChceker = proxyChceker;
		this.checkOnFly = checkOnFly;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof List) {
			List<Object> list = (List) arg;
			list.forEach(this::handleProxy);
		} else {
			handleProxy(arg);
		}
	}
	
	private void handleProxy(Object arg) {
		if (!(arg instanceof Proxy)) return;
		
		Proxy proxy = (Proxy) arg;
		if (checkOnFly.get() && !proxy.isChecked())
			proxyChceker.checkProxy(proxy);
		proxyRepo.addProxy(proxy);
	}
}
