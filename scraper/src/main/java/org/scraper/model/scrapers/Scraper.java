package org.scraper.model.scrapers;

import org.scraper.model.Proxy;
import org.scraper.model.web.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Scraper implements Comparable<Scraper> {
	
	protected ScrapeType type;
	
	protected List<Proxy> proxy = new ArrayList<>();
	
	public abstract List<Proxy> scrape(Site site) throws InterruptedException, IOException;
	
	public Integer getScrapedNum() {
		return proxy.size();
	}
	
	public List<Proxy> getScraped() {
		return proxy;
	}
	
	
	public ScrapeType getType() {
		return type;
	}
	
	
	@Override
	public int compareTo(Scraper o){
		int compare = getScrapedNum() - o.getScrapedNum();
		if(compare==0 && getType().ordinal() > o.getType().ordinal()) return -1;
		if(compare==0 && getType().ordinal() < o.getType().ordinal()) return 1;
		return compare;
	}
	
}