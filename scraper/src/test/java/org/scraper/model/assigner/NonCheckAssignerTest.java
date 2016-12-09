package org.scraper.model.assigner;

import org.junit.BeforeClass;
import org.junit.Test;
import org.scraper.model.Proxy;
import org.scraper.model.TestsUtils;
import org.scraper.model.assigner.IAssigner;
import org.scraper.model.assigner.NonCheckAssigner;
import org.scraper.model.scrapers.ScrapeType;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NonCheckAssignerTest {
	
	
	private static IAssigner assigner;
	
	@BeforeClass
	public static void setUp(){
		assigner = new NonCheckAssigner(TestsUtils.methodFinder);
	}
	
	
	/*@Test
	public void getType() throws Exception {
		ScrapeType type = assigner.getType(TestsUtils.cssSite);
		assertEquals(type, ScrapeType.CSS);
	}
	
	@Test
	public void getProxy() throws Exception {
		List<Proxy> proxies = assigner.getProxy();
		assertTrue(!proxies.isEmpty());
	}*/
	
}