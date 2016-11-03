package org.scraper.model.assigner;

import org.apache.commons.collections.ListUtils;
import org.scraper.model.Pool;
import org.scraper.model.Proxy;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.scrapers.Scraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckingAssigner extends NonCheckAssigner {
	
	private ProxyChecker checker;
	
	private ScrapeType type = ScrapeType.BLACK;
	
	public CheckingAssigner(ScrapersFactory scrapersFactory, ProxyChecker checker) {
		super(scrapersFactory);
		this.checker = checker;
	}
	
	public ScrapeType getType(Site site) {
		type = super.getType(site);
		AvgAssigner.assignAvg(site, proxy, checker);
		return type;
	}
	
	
}
