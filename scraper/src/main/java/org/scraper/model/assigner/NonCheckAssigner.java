package org.scraper.model.assigner;

import org.scraper.model.Proxy;
import org.scraper.model.modles.MainModel;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.scrapers.Scraper;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;

public class NonCheckAssigner implements IAssigner {
	
	private IScrapeMethodFinder scrapeMethodFinder;
	
	private List<Proxy> proxy = new ArrayList<>();
	
	public NonCheckAssigner(IScrapeMethodFinder scrapeMethodFinder) {
		this.scrapeMethodFinder = scrapeMethodFinder;
	}
	
	@Override
	public ScrapeType getType(Site site) {
		String address = site.getAddress();
		
		MainModel.log.info("Getting non check {} scrapng type", address);
		
		Scraper winner = scrapeMethodFinder.findBest(site);
		
		proxy.addAll(winner.getScraped());
		AvgAssigner.assignAvg(site, proxy);
		
		return proxy.size() > 0 ? winner.getType() : ScrapeType.BLACK;
	}
	
	@Override
	public List<Proxy> getProxy() {
		return proxy;
	}
}
