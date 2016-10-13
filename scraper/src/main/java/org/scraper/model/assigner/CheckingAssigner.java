package org.scraper.model.assigner;

import org.apache.commons.collections.ListUtils;
import org.scraper.model.Pool;
import org.scraper.model.Proxy;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.scrapers.Scraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Site;

import java.util.List;

public class CheckingAssigner extends NonCheckAssigner {
	
	private ProxyChecker checker;
	
	private ScrapeType type;
	private double minWorkingPrecent = 0.2, last = 0D;
	
	public static void main(String[] args) {
	}
	
	public CheckingAssigner(ScrapersFactory scrapersFactory, ProxyChecker checker) {
		super(scrapersFactory);
		
		this.checker = checker;
	}
	
	public CheckingAssigner(int size, int timeout) {
		super(new ScrapersFactory(size));
		
		this.checker = new ProxyChecker(new Pool(size), timeout);
	}
	
	public ScrapeType getType(Site address) {
		
		super.scrapeAll(address);
		
		scrapers.forEach(scraper -> {
			double present = workingPrecent(scraper);
			if (present > last) {
				type = scraper.getType();
				last = present;
			}
		});
		
		return type;
	}
	
	private double workingPrecent(Scraper scraper) {
		List<Proxy> prxs = checker.checkProxies(ListUtils.subtract(scraper.getScraped(), proxy));
		int all = prxs.size();
		int workingSize;
		
		List<Proxy> working = checker.checkProxies(prxs);
		proxy.addAll(working);
		
		workingSize = working.size();
		
		double precent = (double) workingSize / (double) all;
		
		return precent;
	}
	
	public void setMinWorkingPrecent(double minWorkingPrecent) {
		this.minWorkingPrecent = minWorkingPrecent;
	}
}
