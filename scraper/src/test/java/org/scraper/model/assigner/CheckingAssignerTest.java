package org.scraper.model.assigner;

import org.junit.BeforeClass;
import org.junit.Test;
import org.scraper.model.Proxy;
import org.scraper.model.TestsUtils;
import org.scraper.model.scrapers.ScrapeType;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CheckingAssignerTest {
	
	private static IAssigner checkingAssigner;
	
	@BeforeClass
	public static void setUp(){
		checkingAssigner = new CheckingAssigner(TestsUtils.methodFinder, TestsUtils.checker);
	}
	
	@Test
	public void getType() throws Exception {
		ScrapeType type = checkingAssigner.getType(TestsUtils.normalSite);
		assertEquals(ScrapeType.NORMAL, type);
	}
	
	@Test
	public void getProxy() throws Exception {
		List<Proxy> proxies = checkingAssigner.getProxy();
		assertTrue(!proxies.isEmpty());
	}
	
}