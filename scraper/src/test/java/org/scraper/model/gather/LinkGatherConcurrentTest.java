package org.scraper.model.gather;

import org.junit.Test;
import org.scraper.model.TestsUtils;
import org.scraper.model.web.Site;

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