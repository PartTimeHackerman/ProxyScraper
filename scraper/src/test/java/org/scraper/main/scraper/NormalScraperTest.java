package org.scraper.main.scraper;

import org.junit.Test;
import org.scraper.main.Proxy;
import org.scraper.main.TestsUtils;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class NormalScraperTest extends ScrapersTest {
	
	@Test
	public void scrape() throws Exception {
		Scraper normalScraper = new NormalScraper();
		normalScraper.setMatcher(new PairMatcher());
		List<Proxy> scraped = normalScraper.scrape(TestsUtils.normalSite);
		
		assertTrue(!scraped.isEmpty());
	}
	
}