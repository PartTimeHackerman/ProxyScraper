package org.scraper.model.scrapers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.scraper.model.Proxy;

import java.util.List;

public class NormalScraperTest extends ScrapersTest {
	
	@Test
	public void scrape() throws Exception {
		Scraper normalScraper = new NormalScraper();
		normalScraper.setMatcher(new PairMatcher());
		List<Proxy> scraped = normalScraper.scrape(testSite);
		
		assertTrue(scraped.size() == 3);
		scraped = normalScraper.scrape(testSiteReversed);
		assertTrue(scraped.size() == 3);
		assertEquals(firstProxy, scraped.get(0).getIpPort());
	}
	
}