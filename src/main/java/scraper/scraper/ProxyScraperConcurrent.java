package scraper.scraper;

import scraper.IConcurrent;
import scraper.Pool;
import scraper.Proxy;
import scraper.data.Site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

public class ProxyScraperConcurrent extends ProxyScraper implements IConcurrent {
	
	private Pool pool;
	
	public ProxyScraperConcurrent(ScrapersFactory scrapersFactory, Pool pool) {
		super(scrapersFactory);
		this.pool = pool;
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		return pool.sendTaskOrRunIfFull(() -> super.scrape(site), false);
	}
	
	@Override
	public List<Proxy> scrapeList(Collection<Site> sites) {
		List<Callable<List<Proxy>>> calls = new ArrayList<>();
		
		
		sites.forEach(site ->
							  calls.add(() ->
												super.scrape(site)));
		List<List<Proxy>> proxyList;
		
		proxyList = pool.getNewSubPool().sendTasksOrRunIfFull(calls);
		List<Proxy> proxy = new ArrayList<>();
		proxyList.forEach(proxy::addAll);
		return proxy;
	}
}
