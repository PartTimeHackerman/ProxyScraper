package org.scraper.model.assigner;

import org.scraper.model.Globals;
import org.scraper.model.Main;
import org.scraper.model.scraper.ScrapeType;
import org.scraper.model.scraper.Scraper;
import org.scraper.model.scraper.ScrapersFactory;
import org.scraper.model.web.Site;

import java.io.IOException;
import java.util.*;

public class NonCheckAssigner extends Assigner {
	
	protected List<Scraper> scrapers;
	
	public static void main(String[] args) throws InterruptedException {
		Globals g = new Globals();
		NonCheckAssigner na = new NonCheckAssigner(g.getScrapersFactory());
		ScrapeType t = na.getType(new Site("https://www.torvpn.com/en/proxy-list",ScrapeType.UNCHECKED));
	}
	
	public NonCheckAssigner(ScrapersFactory scrapersFactory){
		this.scrapersFactory = scrapersFactory;
	}
	
	public NonCheckAssigner(int size){
		this.scrapersFactory = new ScrapersFactory(size);
	}
	
	@Override
	public ScrapeType getType(Site address) {
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
