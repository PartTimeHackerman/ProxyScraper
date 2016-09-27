package org.scraper.model.assigner;

import org.apache.commons.collections.ListUtils;
import org.scraper.model.Globals;
import org.scraper.model.Pool;
import org.scraper.model.Proxy;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.scraper.ScrapeType;
import org.scraper.model.scraper.Scraper;
import org.scraper.model.scraper.ScrapersFactory;
import org.scraper.model.web.Site;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CheckingAssigner extends NonCheckAssigner implements Observer {
	
	private ProxyChecker checker;
	
	private ScrapeType type;
	private int minAll = 10, minWorking = 0;
	private double minWorkingPrecent = 0.2, last = 0D;
	
	public static void main(String[] args) {
		Globals g = new Globals();
		Assigner ca = new CheckingAssigner(g.getScrapersFactory(), new ProxyChecker(g.getGlobalPool(),5000));
		
		ScrapeType t = ca.getType(new Site("http://proxylist.hidemyass.com/",ScrapeType.UNCHECKED));
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
	
	@Override
	public void update(Observable o, Object arg) {
		proxy.add((Proxy) arg);
	}
	
	public void setMinAll(int minAll) {
		this.minAll = minAll;
	}
	
	public void setMinWorking(int minWorking) {
		this.minWorking = minWorking;
	}
	
	public void setMinWorkingPrecent(double minWorkingPrecent) {
		this.minWorkingPrecent = minWorkingPrecent;
	}
}
