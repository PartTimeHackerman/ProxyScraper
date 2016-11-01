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
	
	public static void main(String[] args) {
	}
	
	public CheckingAssigner(ScrapersFactory scrapersFactory, ProxyChecker checker) {
		super(scrapersFactory);
		
		this.checker = checker;
	}
	
	public CheckingAssigner(int size, int timeout) {
		super(new ScrapersFactory(size));
		
		this.checker = new ProxyChecker(new Pool(size), timeout, new ArrayList<>());
	}
	
	public ScrapeType getType(Site site) {
		
		type = super.getType(site);
		
		double workingPrecent = workingPrecent(proxy, checker);
		
		/*scrapers.forEach(scraper -> {
			present = workingPrecent(scraper);
			if (present > last) {
				type = scraper.getType();
				last = present;
			}
		});
		*/
		Assigner.setAvgWorking(site, workingPrecent);
		
		return type;
	}
	
	public static double workingPrecent(List<Proxy> proxies, ProxyChecker checker) {
		int all = proxies.size();
		int workingSize = checker.checkProxies(proxies, true)
				.stream()
				.filter(Proxy::isWorking)
				.collect(Collectors.toList())
				.size();
		
		return workingSize / (double) all;
	}
	
	/*public void setMinWorkingPrecent(double minWorkingPrecent) {
		this.minWorkingPrecent = minWorkingPrecent;
	}*/
}
