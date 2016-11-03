package org.scraper.model.assigner;

import org.scraper.model.modles.MainModel;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.scrapers.Scraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Site;

import java.util.Collections;
import java.util.List;

public class NonCheckAssigner extends Assigner {
	
	protected List<Scraper> scrapers;
	
	public NonCheckAssigner(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	@Override
	public ScrapeType getType(Site site) {
		String address = site.getAddress();
		
		MainModel.log.info("Getting non check {} scrapng type", address);
		
		Scraper winner = scrapeByAll(site);
		
		proxy = winner.getScraped();
		AvgAssigner.assignAvg(site, proxy);
		
		return proxy.size() > 0 ? winner.getType() : ScrapeType.BLACK;
	}
	
	private Scraper scrapeByAll(Site site) {
		
		if (site.getType() != ScrapeType.UNCHECKED
				&& site.getType() != ScrapeType.BLACK) {
			Scraper scraper = scrapersFactory.get(site.getType());
			scraper.scrape(site);
			return scraper;
			
		}
		scrapers = scrapersFactory.getAll();
		
		scrapers.forEach(scraper ->
								 scraper.scrape(site));
		
		Collections.sort(scrapers, Collections.reverseOrder());
		return scrapers.get(0);
	}
}
