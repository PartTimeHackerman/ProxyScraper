package scraper.assigner;

import scraper.MainLogger;
import scraper.Proxy;
import scraper.data.Site;
import scraper.scraper.ScrapeType;

import java.util.List;

public class NonCheckAssigner implements IAssigner {
	
	private IScrapeMethodFinder methodFinder;
	
	public NonCheckAssigner(IScrapeMethodFinder methodFinder) {
		this.methodFinder = methodFinder;
	}
	
	@Override
	public ScrapeType getType(Site site) {
		String address = site.getAddress();
		MainLogger.log(this).info("Getting non assign {} scraping type", address);
		
		ScrapeType type = methodFinder.findBest(site);
		
		AvgAssigner.assignAvg(site, getProxy());
		
		return type;
	}
	
	@Override
	public List<Proxy> getProxy() {
		return methodFinder.getList();
	}
}
