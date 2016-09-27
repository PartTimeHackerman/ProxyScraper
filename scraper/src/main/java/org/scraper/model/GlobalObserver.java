package org.scraper.model;

import java.util.Observable;
import java.util.Observer;

public class GlobalObserver implements Observer {
	
	private ProxyManager proxyManager;
	
	public GlobalObserver(ProxyManager proxyManager){
		this.proxyManager = proxyManager;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Proxy) {
			Proxy proxy = (Proxy) arg;
			proxyManager.addProxy(proxy);
			//globals.getLinksManager().clickAll(proxyManager);
		}
	}
}
