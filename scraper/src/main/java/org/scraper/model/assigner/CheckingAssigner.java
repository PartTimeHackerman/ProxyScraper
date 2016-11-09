package org.scraper.model.assigner;

import org.scraper.model.checker.IProxyChecker;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.Site;

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
