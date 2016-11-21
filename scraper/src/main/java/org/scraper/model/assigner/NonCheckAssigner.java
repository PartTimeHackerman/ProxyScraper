package org.scraper.model.assigner;

import org.scraper.model.MainLogger;
import org.scraper.model.Proxy;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.Site;

import java.util.List;

public class NonCheckAssigner implements IAssigner {
	
	private IScrapeMethodFinder methodFinder;
	
	public NonCheckAssigner(IScrapeMethodFinder methodFinder) {
		this.methodFinder = methodFinder;
	}
	
	@Override
	public ScrapeType getType(Site site) {
		String address = site.getAddress();
		MainLogger.log().info("Getting non check {} scrapng type", address);
		
		ScrapeType type = methodFinder.findBest(site);
		
		AvgAssigner.assignAvg(site, getProxy());
		
		return type;
	}
	
	@Override
	public List<Proxy> getProxy() {
		return methodFinder.getList();
	}
}
