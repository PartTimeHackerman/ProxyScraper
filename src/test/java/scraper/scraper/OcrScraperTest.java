package scraper.scraper;

import org.junit.Test;
import scraper.Pool;
import scraper.Proxy;
import scraper.main.TestsUtils;
import scraper.manager.QueuesManager;
import scraper.web.ConcurrentConnectionExecutor;
import scraper.web.ConnectionChecker;

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