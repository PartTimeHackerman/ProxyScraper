package org.scraper.main.scraper;

import org.scraper.main.MainLogger;
import org.scraper.main.Proxy;
import org.scraper.main.limiter.Switchable;
import org.scraper.main.assigner.AvgAssigner;
import org.scraper.main.manager.AssignManager;
import org.scraper.main.data.Site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;


public class ProxyScraper extends Observable implements Switchable {
	
	private ScrapersFactory scrapersFactory;
	
	private AssignManager assigner;
	
	private Boolean on = true;
	
	public ProxyScraper(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	public List<Proxy> scrape(Site site) {
		List<Proxy> proxy = new ArrayList<>();
		
		if (!isOn())
			return proxy;
		
		if (assigner != null && site.getType() == ScrapeType.UNCHECKED)
			assigner.assign(site);
		
		ScraperAbstract scraper = scrapersFactory.get(site.getType());
		
		proxy.addAll(scraper.scrape(site));
		AvgAssigner.assignAvg(site, proxy);
		
		setChanged();
		notifyObservers(proxy);
		
		return proxy;
	}
	
	public List<Proxy> scrapeList(Collection<Site> sites) {
		List<List<Proxy>> proxyList = new ArrayList<>(sites.size());
		
		sites.forEach(site ->
							  proxyList.add(scrape(site)));
		
		List<Proxy> proxy = new ArrayList<>();
		proxyList.forEach(proxy::addAll);
		
		MainLogger.log().info("List scraping done");
		return proxy;
	}
	
	public void setAssigner(AssignManager assigner) {
		this.assigner = assigner;
	}
	
	@Override
	public void turnOn() {
		on = true;
	}
	
	@Override
	public void turnOff() {
		on = false;
	}
	
	@Override
	public Boolean isOn() {
		return on;
	}
}
