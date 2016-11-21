package org.scraper.model.scrapers;

import org.junit.Test;
import org.scraper.model.Proxy;
import org.scraper.model.TestsUtils;
import org.scraper.model.web.ConnectionChecker;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class OcrScraperTest {
	
	@Test
	public void scrape() throws Exception {
		
		assertTrue(ConnectionChecker.hasConnection());
		Scraper cssScraper = new OcrScraperConcurrentWQueue();
		List<Proxy> proxies = cssScraper.scrape(TestsUtils.ocrSite);
		assertTrue(!proxies.isEmpty());
	}
	
}