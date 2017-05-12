package scraper.scraper;

import org.junit.Test;
import scraper.Proxy;
import scraper.main.TestsUtils;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class NormalScraperTest extends ScrapersTest {
	
	@Test
	public void scrape() throws Exception {
		ScraperAbstract normalScraper = new NormalScraper();
		normalScraper.setMatcher(new PairMatcher());
		List<Proxy> scraped = normalScraper.scrape(TestsUtils.normalSite);
		
		assertTrue(!scraped.isEmpty());
	}
	
}