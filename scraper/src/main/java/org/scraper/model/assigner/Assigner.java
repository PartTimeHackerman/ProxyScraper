package org.scraper.model.assigner;

import org.scraper.model.Proxy;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.scrapers.ScrapersFactory;
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
	
	public static void setAvgProxies(Site site, Integer proxies) {
		site.setAvgSites(site.getAvgSites() == null ? (site.getAvgSites() + proxies) / 2 : proxies);
	}
	
	public static void setAvgWorking(Site site, Double workingPrecent) {
		site.setAvgWorking(site.getAvgWorking() > 0 ? ((site.getAvgWorking() + (int) (site.getAvgSites() * workingPrecent)) / 2) : (int) (site.getAvgSites() * workingPrecent));
	}
}
