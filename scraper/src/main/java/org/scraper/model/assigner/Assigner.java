package org.scraper.model.assigner;

import org.scraper.model.Proxy;
import org.scraper.model.scraper.ScrapeType;
import org.scraper.model.scraper.ScrapersFactory;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;

public abstract class Assigner {
	
	protected List<Proxy> proxy = new ArrayList<>();
	
	protected ScrapersFactory scrapersFactory;
	
	public abstract ScrapeType getType(Site address);
	
	public List<Proxy> getProxy() {
		return proxy;
	}
}
