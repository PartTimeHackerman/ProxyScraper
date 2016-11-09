package org.scraper.model.web;

import org.junit.Before;
import org.junit.Test;
import org.scraper.model.scrapers.ScrapeType;

import static org.junit.Assert.assertEquals;

public class SiteTest {
	
	Site site;
	
	String address = "www.test.xyz";
	String formed = "http://www.test.xyz/";
	ScrapeType type = ScrapeType.NORMAL;
	Integer avg = 100;
	
	
	@Before
	public void setUp() throws Exception {
		site = new Site(address, type);
		
	}
	
	@Test
	public void getAddress() throws Exception {
		assertEquals(formed, site.getAddress());
		
		site = new Site(formed, ScrapeType.NORMAL);
		assertEquals(formed, site.getAddress());
	}
	
	
	@Test
	public void getType() throws Exception {
		assertEquals(type, site.getType());
	}
	
	@Test
	public void setType() throws Exception {
		site.setType(ScrapeType.OCR);
		assertEquals(ScrapeType.OCR, site.getType());
	}
	
	@Test
	public void getDomain() throws Exception {
		Domain domain = new Domain("www.test.xyz", ScrapeType.NORMAL);
		assertEquals(domain, site.getDomain());
	}
	
	@Test
	public void getRoot() throws Exception {
		assertEquals("http://www.test.xyz", site.getRoot());
	}
	
	@Test
	public void equals() throws Exception {
		assertEquals(new Site(address, ScrapeType.CSS), site);
	}
	
	@Test
	public void getSetAvgWorking() throws Exception {
		site.setAvgWorking(avg);
		assertEquals(avg, site.getAvgWorking());
	}
	
	@Test
	public void getSetAvgSites() throws Exception {
		site.setAvgSites(avg);
		assertEquals(avg, site.getAvgSites());
	}
	
}