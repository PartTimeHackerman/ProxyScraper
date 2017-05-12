package scraper.scraper;

import scraper.MainLogger;
import scraper.Proxy;
import scraper.data.Site;

import java.util.ArrayList;
import java.util.List;

class NullScraper extends ScraperAbstract {
	
	NullScraper(){
		type = ScrapeType.BLACK;
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		MainLogger.log(this).warn("URL {} doesn't contain any proxies", site.getAddress());
		return new ArrayList<>();
	}
	
}
