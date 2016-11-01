package org.scraper.model.assigner;

import org.scraper.model.Main;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.scrapers.Scraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Site;

import java.io.IOException;
import java.util.*;

public class NonCheckAssigner extends Assigner {
	
	protected List<Scraper> scrapers;
	
	public static void main(String[] args) throws InterruptedException {
	}
	
	public NonCheckAssigner(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	public NonCheckAssigner(int size) {
		this.scrapersFactory = new ScrapersFactory(size);
	}
	
	@Override
	public ScrapeType getType(Site site) {
		String address = site.getAddress();
		
		
		Main.log.info("Getting non check {} scrapng type", address);
		
		Scraper winner = scrapeAll(site);
		
		proxy = winner.getScraped();
		Assigner.setAvgProxies(site,proxy.size());
		
		return proxy.size() > 0 ? winner.getType() : ScrapeType.BLACK;
	}
	
	protected Scraper scrapeAll(Site address) {
		scrapers = scrapersFactory.getAll();
		
		scrapers.forEach(scraper -> {
			try {
				scraper.scrape(address);
			} catch (InterruptedException | IOException e) {
				Main.log.error("Failed to get type, error: " + (e.getMessage() != null ? e.getMessage() : "null"));
			}
		});
		Collections.sort(scrapers, Collections.reverseOrder());
		return scrapers.get(0);
	}
}
