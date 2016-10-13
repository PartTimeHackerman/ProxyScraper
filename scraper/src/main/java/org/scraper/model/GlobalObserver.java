package org.scraper.model;

import org.scraper.model.assigner.AssignManager;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.managers.ProxyManager;
import org.scraper.model.managers.SitesManager;
import org.scraper.model.managers.ProxyTableManager;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.Site;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class GlobalObserver implements Observer {
	
	private ProxyManager proxyManager;
	
	private SitesManager sitesManager;
	
	private AssignManager assignManager;
	private ProxyChecker proxyChceker;
	private Boolean checkOnFly;
	
	public GlobalObserver(ProxyManager proxyManager,
						  SitesManager sitesManager,
						  AssignManager assignManager,
						  ProxyChecker proxyChecker,
						  Boolean checkOnFly) {
		this.proxyManager = proxyManager;
		this.sitesManager = sitesManager;
		this.assignManager = assignManager;
		this.proxyChceker = proxyChecker;
		this.checkOnFly = checkOnFly;
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
		if (checkOnFly && site.getType() == ScrapeType.UNCHECKED)
			assignManager.assign(site);
		sitesManager.addSite(site);
		
		//ProxyTableManager.addProxy(site);
	}
	
	private void handleProxy(Object arg) {
		if (!(arg instanceof Proxy)) return;
		
		Proxy proxy = (Proxy) arg;
		if (checkOnFly && !proxy.isChecked())
			proxyChceker.checkProxy(proxy);
		proxyManager.addProxy(proxy);
		
		ProxyTableManager.addProxy(proxy);
	}
}
