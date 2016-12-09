package org.scraper.model.scrapers;

import org.junit.Test;
import org.scraper.model.Proxy;
import org.scraper.model.web.Browser;
import org.scraper.model.web.BrowserConcurrent;
import org.scraper.model.web.ConnectionChecker;
import org.scraper.model.web.Site;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class CssScraperTest {
	
	private final Browser browser = new BrowserConcurrent();
	private final Site site = new Site("http://proxylist.hidemyass.com/", ScrapeType.CSS);
	
	/*@Test
	public void scrape() throws Exception {
		assertTrue(ConnectionChecker.hasConnection());
		Scraper cssScraper = new CssScraper(browser);
		List<Proxy> proxies = cssScraper.scrape(site);
		assertTrue(!proxies.isEmpty());
	}*/
	
}