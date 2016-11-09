package org.scraper.model.assigner;

import org.scraper.model.Proxy;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.Site;

import java.util.List;

public interface IAssigner {
	
	ScrapeType getType(Site address);
	
	List<Proxy> getProxy();
}
