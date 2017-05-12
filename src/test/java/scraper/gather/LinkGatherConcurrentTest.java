package scraper.gather;

import org.junit.Test;
import scraper.main.TestsUtils;
import scraper.data.Site;

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