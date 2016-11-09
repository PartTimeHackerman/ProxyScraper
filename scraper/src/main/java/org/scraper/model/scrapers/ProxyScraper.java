package org.scraper.model.scrapers;

import org.scraper.model.IPool;
import org.scraper.model.Proxy;
import org.scraper.model.managers.AssignManager;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


public class ProxyScraper extends Observable {
	
	private ScrapersFactory scrapersFactory;
	
	private AssignManager assigner;
	
	public ProxyScraper(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	public List<Proxy> scrape(Site site) {
		List<Proxy> proxy = new ArrayList<>();
				
				if (assigner != null && site.getType() == ScrapeType.UNCHECKED)
					assigner.assign(site);
				
				Scraper scraper = scrapersFactory.get(site.getType());
				
				proxy.addAll(scraper.scrape(site));
				
				setChanged();
				notifyObservers(proxy);
				
		return proxy;
	}
	
	public List<Proxy> scrapeList(List<Site> sites) {
		List<List<Proxy>> proxyList = new ArrayList<>(sites.size());
		
		sites.forEach(site ->
							  proxyList.add(scrape(site)));
		
		List<Proxy> proxy = new ArrayList<>();
		proxyList.forEach(proxy::addAll);
		
		return proxy;
	}
	
	public void setAssigner(AssignManager assigner) {
		this.assigner = assigner;
	}
	
}
