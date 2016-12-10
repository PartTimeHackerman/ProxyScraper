package org.scraper.main.assigner;

import org.scraper.main.Proxy;
import org.scraper.main.scraper.ScrapeType;
import org.scraper.main.scraper.Scraper;
import org.scraper.main.scraper.ScrapersFactory;
import org.scraper.main.web.Site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BestOfAllFinder implements IScrapeMethodFinder {
	
	private ScrapersFactory scrapersFactory;
	
	private List<Proxy> proxies = new ArrayList<>();
	
	public BestOfAllFinder(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	@Override
	public ScrapeType findBest(Site site) {
		
		if (site.getType() != ScrapeType.UNCHECKED
				&& site.getType() != ScrapeType.BLACK) {
			Scraper scraper = scrapersFactory.get(site.getType());
			scraper.scrape(site);
			proxies = scraper.getScraped();
			return scraper.getType();
		}
		
		List<Scraper> scrapers = scrapersFactory.getAll();
		
		scrapers.forEach(scraper ->
								 scraper.scrape(site));
		
		Collections.sort(scrapers, Collections.reverseOrder());
		Scraper winner = scrapers.get(0);
		proxies = winner.getScraped();
		return winner.getType();
	}
	
	@Override
	public List<Proxy> getList() {
		return proxies;
	}
}
