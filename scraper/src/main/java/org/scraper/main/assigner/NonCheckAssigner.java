package org.scraper.main.assigner;

import org.scraper.main.MainLogger;
import org.scraper.main.Proxy;
import org.scraper.main.scraper.ScrapeType;
import org.scraper.main.data.Site;

import java.util.List;

public class NonCheckAssigner implements IAssigner {
	
	private IScrapeMethodFinder methodFinder;
	
	public NonCheckAssigner(IScrapeMethodFinder methodFinder) {
		this.methodFinder = methodFinder;
	}
	
	@Override
	public ScrapeType getType(Site site) {
		String address = site.getAddress();
		MainLogger.log().info("Getting non assgn {} scrapng type", address);
		
		ScrapeType type = methodFinder.findBest(site);
		
		AvgAssigner.assignAvg(site, getProxy());
		
		return type;
	}
	
	@Override
	public List<Proxy> getProxy() {
		return methodFinder.getList();
	}
}
