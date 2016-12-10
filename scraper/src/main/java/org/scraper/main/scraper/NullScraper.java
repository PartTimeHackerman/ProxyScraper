package org.scraper.main.scraper;

import org.scraper.main.MainLogger;
import org.scraper.main.Proxy;
import org.scraper.main.web.Site;

import java.util.ArrayList;
import java.util.List;

class NullScraper extends Scraper {
	
	NullScraper(){
		type = ScrapeType.BLACK;
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		MainLogger.log().warn("URL {} doesn't contain any proxies", site.getAddress());
		return new ArrayList<>();
	}
	
}
