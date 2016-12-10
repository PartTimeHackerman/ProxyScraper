package org.scraper.main.assigner;

import org.scraper.main.checker.IProxyChecker;
import org.scraper.main.scraper.ScrapeType;
import org.scraper.main.web.Site;

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
