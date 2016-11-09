package org.scraper.model.scrapers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scraper.model.Proxy;
import org.scraper.model.web.Browser;
import org.scraper.model.web.ConnectionChecker;
import org.scraper.model.web.Site;

import java.util.List;

public class CssScraperTest {
	
	private final Browser browser = new Browser();
	private final Site site = new Site("http://proxylist.hidemyass.com/", ScrapeType.CSS);
	
	@Test
	public void scrape() throws Exception {
		assertTrue(ConnectionChecker.hasConnection());
		Scraper cssScraper = new CssScraper(browser);
		List<Proxy> proxies = cssScraper.scrape(site);
		assertTrue(!proxies.isEmpty());
		
	}
	
}