package org.scraper.main.gather;

import org.junit.Test;
import org.scraper.main.TestsUtils;
import org.scraper.main.web.Site;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class LinkGatherConcurrentTest {
	
	LinksGather linksGather = new LinkGatherConcurrent(1);
	
	@Test
	public void gather() throws Exception {
		
		List<Site> gathered = linksGather.gather(TestsUtils.normalSite);
		
		assertTrue(!gathered.isEmpty());
		
	}
	
}