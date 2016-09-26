package org.scraper.model.scrapers;

import org.scraper.model.Main;
import org.scraper.model.Proxy;
import org.scraper.model.web.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NullScraper extends Scraper {
	
	public NullScraper(){
		type = ScrapeType.BLACK;
	}
	
	@Override
	public List<Proxy> scrape(Site site) throws InterruptedException, IOException {
		Main.log.warn("URL {} doesn't contain any proxies", site.getAddress());
		return new ArrayList<>();
	}
	
}
