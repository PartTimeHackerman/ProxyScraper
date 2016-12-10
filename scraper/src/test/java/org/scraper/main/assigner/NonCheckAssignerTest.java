package org.scraper.main.assigner;

import org.junit.BeforeClass;
import org.scraper.main.TestsUtils;

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