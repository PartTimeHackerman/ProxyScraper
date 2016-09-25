package org.scraper.comp.scrapers;

import org.scraper.comp.Main;
import org.scraper.comp.Proxy;
import org.scraper.comp.web.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NullScraper extends Scraper {
	
	public NullScraper(){
		type = ScrapeType.BLACK;
	}
	
	@Override
	public List<Proxy> scrape(Site site) throws InterruptedException, IOException {
		Main.log.warn("URL {} doesn't contain any proxies", site);
		return new ArrayList<>();
	}
	
}
