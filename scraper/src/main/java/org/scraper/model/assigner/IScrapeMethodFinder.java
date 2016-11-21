package org.scraper.model.assigner;

import org.scraper.model.Proxy;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.Site;

import java.util.List;

public interface IScrapeMethodFinder {
	
	
	ScrapeType findBest(Site site);
	
	List<Proxy> getList();
	
}
