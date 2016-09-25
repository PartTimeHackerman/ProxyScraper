package org.scraper.comp.assigner;

import org.scraper.comp.Proxy;
import org.scraper.comp.scrapers.ScrapeType;
import org.scraper.comp.scrapers.ScrapersFactory;
import org.scraper.comp.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class Assigner {
	
	protected List<Proxy> proxy = new ArrayList<>();
	
	protected ScrapersFactory scrapersFactory;
	
	public abstract ScrapeType getType(Site address) throws InterruptedException;
	
	public List<Proxy> getProxy() {
		return proxy;
	}
}
