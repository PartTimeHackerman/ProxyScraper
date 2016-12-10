package org.scraper.main.scraper;

import org.junit.Test;
import org.scraper.main.Proxy;
import org.scraper.main.TestsUtils;
import org.scraper.main.web.ConnectionChecker;

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