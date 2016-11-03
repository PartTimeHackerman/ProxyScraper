package org.scraper.model.scrapers;

import org.scraper.model.Proxy;
import org.scraper.model.modles.MainModel;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;

public class NullScraper extends Scraper {
	
	public NullScraper(){
		type = ScrapeType.BLACK;
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		MainModel.log.warn("URL {} doesn't contain any proxies", site.getAddress());
		return new ArrayList<>();
	}
	
}
