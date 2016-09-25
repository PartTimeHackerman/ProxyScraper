package org.scraper.comp.assigner;

import org.scraper.comp.Main;
import org.scraper.comp.scrapers.ScrapeType;
import org.scraper.comp.scrapers.Scraper;
import org.scraper.comp.scrapers.ScrapersFactory;
import org.scraper.comp.web.Site;

import java.io.IOException;
import java.util.*;

public class NonCheckAssigner extends Assigner {
	
	private List<Scraper> scrapers;
	
	public static void main(String[] args) throws InterruptedException {
	}
	
	public NonCheckAssigner(ScrapersFactory scrapersFactory){
		this.scrapersFactory = scrapersFactory;
	}
	
	@Override
	public ScrapeType getType(Site address) throws InterruptedException {
		String site = address.getAddress();
		
		
		Main.log.info("Getting non check {} scrapng type", site);
		
		scrapeAll(address);
		
		Scraper winnerScraper = scrapers.get(0);
		proxy = winnerScraper.getScraped();
		
		return winnerScraper.getType();
	}
	
	protected void scrapeAll(Site address){
		scrapers = scrapersFactory.getAll();
		
		scrapers.forEach(scraper -> {
			try {
				scraper.scrape(address);
			} catch (InterruptedException | IOException e) {
				Main.log.error("Failed to get type, error: " + (e.getMessage() != null ? e.getMessage() : "null"));
			}
		});
		
		Collections.sort(scrapers, Collections.reverseOrder());
	}
}
