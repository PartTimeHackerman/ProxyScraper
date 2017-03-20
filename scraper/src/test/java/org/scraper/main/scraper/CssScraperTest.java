package org.scraper.main.scraper;

import org.scraper.main.web.Browser;
import org.scraper.main.web.BrowserConcurrent;
import org.scraper.main.data.Site;

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