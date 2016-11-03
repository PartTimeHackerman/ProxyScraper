package org.scraper.model.scrapers;

import org.scraper.model.Pool;
import org.scraper.model.Proxy;
import org.scraper.model.managers.AssignManager;
import org.scraper.model.web.Domain;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


public class ProxyScraper extends Observable {
	
	private ScrapersFactory scrapersFactory;
	
	private Pool pool;
	
	private AssignManager assigner;
	
	private List<Domain> domains;
	
	public ProxyScraper(ScrapersFactory scrapersFactory, Pool pool, List<Domain> domains) {
		this.scrapersFactory = scrapersFactory;
		this.pool = pool;
		this.domains = domains;
	}
	
	public ProxyScraper(int size) {
		this.scrapersFactory = new ScrapersFactory(size);
	}
	
	public List<Proxy> scrapeConcurrent(Site site, Boolean wait) {
		return pool.sendTask(() -> scrape(site), wait);
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
		
		List<Callable<List<Proxy>>> calls = new ArrayList<>();
		List<List<Proxy>> proxyList;
		
		sites.stream()
				.map(site ->
							 calls.add(() -> scrape(site)))
				.collect(Collectors.toList());
		
		proxyList = pool.sendTasks(calls);
		
		List<Proxy> proxy = new ArrayList<>();
		
		proxyList.forEach(proxy::addAll);
		
		return proxy;
	}
	
	public void setAssigner(AssignManager assigner) {
		this.assigner = assigner;
	}
	
}
