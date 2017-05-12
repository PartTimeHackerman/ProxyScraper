package scraper.assigner;

import scraper.checker.IProxyChecker;
import scraper.scraper.ScrapeType;
import scraper.data.Site;

public class CheckingAssigner extends NonCheckAssigner {
	
	private IProxyChecker checker;
	
	public CheckingAssigner(IScrapeMethodFinder scrapeMethodFinder, IProxyChecker checker) {
		super(scrapeMethodFinder);
		this.checker = checker;
	}
	
	public ScrapeType getType(Site site) {
		ScrapeType type = super.getType(site);
		AvgAssigner.assignAvg(site, getProxy(), checker);
		return type;
	}
	
	
}
