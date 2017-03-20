package org.scraper.main.scraper;

import org.scraper.main.Proxy;
import org.scraper.main.manager.QueuesManager;
import org.scraper.main.data.Site;

import java.util.List;

class CssScraperWQueue extends CssScraper {
	
	private QueuesManager queuesManager;
	
	CssScraperWQueue(QueuesManager queuesManager) {
		super();
		this.queuesManager = queuesManager;
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		setBrowser(queuesManager
						   .getBrowserQueue()
						   .take());
		
		List<Proxy> scraped = super.scrape(site);
		
		queuesManager
				.getBrowserQueue()
				.put(browser);
		
		return scraped;
	}
}
