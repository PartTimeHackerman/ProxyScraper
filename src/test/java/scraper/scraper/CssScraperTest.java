package scraper.scraper;

import scraper.data.Site;
import scraper.web.Browser;
import scraper.web.BrowserConcurrent;

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