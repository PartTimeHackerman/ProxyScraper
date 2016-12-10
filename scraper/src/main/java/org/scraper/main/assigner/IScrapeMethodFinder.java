package org.scraper.main.assigner;

import org.scraper.main.Proxy;
import org.scraper.main.scraper.ScrapeType;
import org.scraper.main.web.Site;

import java.util.List;

public interface IScrapeMethodFinder {
	
	
	ScrapeType findBest(Site site);
	
	List<Proxy> getList();
	
}
