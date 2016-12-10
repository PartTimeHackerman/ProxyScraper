package org.scraper.main.scraper;

import org.scraper.main.Proxy;
import org.scraper.main.manager.QueuesManager;
import org.scraper.main.web.Site;

import java.util.List;

class CssScraperWQueue extends CssScraper {
	
	CssScraperWQueue() {
		super();
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		setBrowser(QueuesManager.getInstance()
				.getBrowserQueue()
				.take());
		
		List<Proxy> scraped = super.scrape(site);
		
		QueuesManager.getInstance()
				.getBrowserQueue()
				.put(browser);
		
		return scraped;
	}
}
