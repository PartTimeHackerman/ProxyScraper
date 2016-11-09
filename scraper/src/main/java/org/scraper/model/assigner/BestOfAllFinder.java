package org.scraper.model.assigner;

import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.scrapers.Scraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Site;

import java.util.Collections;
import java.util.List;

public class BestOfAllFinder implements IScrapeMethodFinder {
	
	private ScrapersFactory scrapersFactory;
	
	public BestOfAllFinder(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	@Override
	public Scraper findBest(Site site) {
		
		if (site.getType() != ScrapeType.UNCHECKED
				&& site.getType() != ScrapeType.BLACK) {
			Scraper scraper = scrapersFactory.get(site.getType());
			scraper.scrape(site);
			return scraper;
			
		}
		
		List<Scraper> scrapers = scrapersFactory.getAll();
		
		scrapers.forEach(scraper ->
								 scraper.scrape(site));
		
		Collections.sort(scrapers, Collections.reverseOrder());
		return scrapers.get(0);
	}
}
