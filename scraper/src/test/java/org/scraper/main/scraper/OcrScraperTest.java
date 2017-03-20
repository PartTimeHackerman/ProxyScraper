package org.scraper.main.scraper;

import org.junit.Test;
import org.scraper.main.Pool;
import org.scraper.main.Proxy;
import org.scraper.main.TestsUtils;
import org.scraper.main.manager.QueuesManager;
import org.scraper.main.web.ConcurrentConnectionExecutor;
import org.scraper.main.web.ConnectionChecker;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class OcrScraperTest {
	
	@Test
	public void scrape() throws Exception {
		
		assertTrue(ConnectionChecker.hasConnection());
		ScraperAbstract cssScraper = new OcrScraperConcurrentWQueue(new ConcurrentConnectionExecutor(new Pool()), new QueuesManager(1,1));
		List<Proxy> proxies = cssScraper.scrape(TestsUtils.ocrSite);
		assertTrue(!proxies.isEmpty());
	}
	
}