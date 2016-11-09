package org.scraper.model.scrapers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scraper.model.Proxy;
import org.scraper.model.web.Browser;
import org.scraper.model.web.ConnectionChecker;
import org.scraper.model.web.Site;

import java.util.List;

public class OcrScraperTest {
	
	private final Site site = new Site("https://www.torvpn.com/en/proxy-list", ScrapeType.OCR);
	
	@Test
	public void scrape() throws Exception {
		
		assertTrue(ConnectionChecker.hasConnection());
		Scraper cssScraper = new OcrScraperConcurrentWQueue();
		List<Proxy> proxies = cssScraper.scrape(site);
		assertTrue(!proxies.isEmpty());
	}
	
}