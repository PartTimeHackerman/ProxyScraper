package org.scraper.comp.assigner;

import org.scraper.comp.Main;
import org.scraper.comp.Proxy;
import org.scraper.comp.assigner.Assigner;
import org.scraper.comp.checker.ProxyChecker;
import org.scraper.comp.scrapers.ProxyScraper;
import org.scraper.comp.scrapers.ScrapeType;
import org.scraper.comp.scrapers.ScrapersFactory;
import org.scraper.comp.web.Site;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class CheckingAssigner extends NonCheckAssigner implements Observer {
	
	private ProxyChecker checker;
	
	public CheckingAssigner(ScrapersFactory scrapersFactory, ProxyChecker checker, ProxyScraper scraper) {
		super(scrapersFactory);
		
		this.checker = checker;
	}
	
	public ScrapeType getType(Site address) throws InterruptedException {
		String site = address.getAddress();
		int minAll = 10, minWorking = 0;
		double minWoringPrecent = 0.2;
		ScrapeType type;
		
		checker.addObserver(this);
		Main.log.info("Getting {} scrapng type", site);
		try {
			if (workingPrecent(ScrapeType.NORMAL, site, minAll, minWorking) < minWoringPrecent) {
				if (workingPrecent(ScrapeType.CSS, site, minAll, minWorking) < minWoringPrecent) {
					type = (workingPrecent(ScrapeType.OCR, site, minAll, minWorking) < minWoringPrecent) ? ScrapeType.BLACK : ScrapeType.OCR;
				} else {
					type = ScrapeType.CSS;
				}
			} else {
				type = ScrapeType.NORMAL;
			}
		} catch (IOException e) {
			Main.log.error("Failed to get type, error: " + (e.getMessage() != null ? e.getMessage() : "null"));
			type = ScrapeType.UNCHECKED;
		}
		
		checker.deleteObserver(this);
		return type;
	}
	
	private double workingPrecent(ScrapeType type, String url, int minAll, int minWorking) throws IOException, InterruptedException {
		List<Proxy> prxs = scrapersFactory.get(type).scrape(new Site(url, type));
		int all = prxs.size();
		int working = 0;
		
		//Check if site is a crap full of broken proxies
		if (type != ScrapeType.NORMAL && all > 400) return 0;
		
		if (all > minAll) working = checker.checkProxies(prxs).size();
		
		double precent = (double) working / (double) all;
		
		return working > minWorking ? precent : 0;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		proxy.add((Proxy) arg);
	}
}
