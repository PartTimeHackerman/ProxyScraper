package org.scraper.model;

import org.scraper.model.checker.IProxyChecker;
import org.scraper.model.managers.AssignManager;
import org.scraper.model.managers.ProxyManager;
import org.scraper.model.managers.SitesManager;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.Site;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicBoolean;

public class GlobalObserver implements Observer {
	
	private static GlobalObserver observer;
	
	private ProxyManager proxyManager;
	
	private SitesManager sitesManager;
	
	private AssignManager assignManager;
	private IProxyChecker proxyChceker;
	
	private AtomicBoolean checkOnFly;
	
	private GlobalObserver() {}
	
	public static GlobalObserver getInstance(){
		if(observer==null){
			synchronized (GlobalObserver.class){
				if(observer==null){
					observer = new GlobalObserver();
				}
			}
		}
		return observer;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof List) {
			List list = (List) arg;
			list.forEach(e -> {
				handleSite(e);
				handleProxy(e);
			});
		} else {
			handleSite(arg);
			handleProxy(arg);
		}
	}
	
	private void handleSite(Object arg) {
		if (!(arg instanceof Site)) return;
		
		Site site = (Site) arg;
		if (checkOnFly.get() && site.getType() == ScrapeType.UNCHECKED)
			assignManager.assign(site);
		sitesManager.addSite(site);
	}
	
	private void handleProxy(Object arg) {
		if (!(arg instanceof Proxy)) return;
		
		Proxy proxy = (Proxy) arg;
		if (checkOnFly.get() && !proxy.isChecked())
			proxyChceker.checkProxy(proxy);
		proxyManager.addProxy(proxy);
	}
	
	public void setProxyManager(ProxyManager proxyManager) {
		this.proxyManager = proxyManager;
	}
	
	public void setSitesManager(SitesManager sitesManager) {
		this.sitesManager = sitesManager;
	}
	
	public void setAssignManager(AssignManager assignManager) {
		this.assignManager = assignManager;
	}
	
	public void setProxyChceker(IProxyChecker proxyChceker) {
		this.proxyChceker = proxyChceker;
	}
	
	public void setCheckOnFly(AtomicBoolean checkOnFly) {
		this.checkOnFly = checkOnFly;
	}
}
