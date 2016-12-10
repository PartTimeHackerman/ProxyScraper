package org.scraper.main.scraper;

import org.scraper.main.IConcurrent;
import org.scraper.main.Proxy;
import org.scraper.main.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ProxyScraperConcurrent extends ProxyScraper implements IConcurrent {
	
	public ProxyScraperConcurrent(ScrapersFactory scrapersFactory) {
		super(scrapersFactory);
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		return send(() -> super.scrape(site));
	}
	
	@Override
	public List<Proxy> scrapeList(List<Site> sites) {
		List<Callable<List<Proxy>>> calls = new ArrayList<>();
		List<List<Proxy>> proxyList;
		
		sites.forEach(site ->
							  calls.add(() ->
												super.scrape(site)));
		
		proxyList = sendTasks(calls);
		
		List<Proxy> proxy = new ArrayList<>();
		proxyList.forEach(proxy::addAll);
		return proxy;
	}
}
