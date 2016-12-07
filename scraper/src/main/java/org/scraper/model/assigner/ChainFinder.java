package org.scraper.model.assigner;

import org.scraper.model.Proxy;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.scrapers.Scraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;

public class ChainFinder implements IScrapeMethodFinder {
	
	private ScrapersFactory scrapersFactory;
	
	private List<Proxy> proxies = new ArrayList<>();
	
	private Integer minimumProxies = 10;
	
	public ChainFinder(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	@Override
	public ScrapeType findBest(Site site) {
		proxies.clear();
		if (site.getType() != ScrapeType.UNCHECKED
				&& site.getType() != ScrapeType.BLACK) {
			Scraper scraper = scrapersFactory.get(site.getType());
			scraper.scrape(site);
			proxies = scraper.getScraped();
			return scraper.getType();
		}
		
		List<Scraper> scrapers = scrapersFactory.getAll();
		
		for (Scraper scraper : scrapers) {
			if (scraper.scrape(site).size() >= minimumProxies) {
				proxies = scraper.getScraped();
				return scraper.getType();
			}
		}
		return ScrapeType.BLACK;
	}
	
	@Override
	public List<Proxy> getList() {
		return proxies;
	}
}
