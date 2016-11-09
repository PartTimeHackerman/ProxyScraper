package org.scraper.model.scrapers;

import org.scraper.model.Proxy;
import org.scraper.model.managers.Queue;
import org.scraper.model.managers.QueuesManager;
import org.scraper.model.web.Browser;
import org.scraper.model.web.Site;

import java.util.List;

class CssScraperWQueue extends CssScraper {
	
	CssScraperWQueue() {
		super(QueuesManager.getInstance()
					  .getBrowserQueue()
					  .take());
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		List<Proxy> scraped = super.scrape(site);
		QueuesManager.getInstance()
				.getBrowserQueue()
				.put(browser);
		return scraped;
	}
}
