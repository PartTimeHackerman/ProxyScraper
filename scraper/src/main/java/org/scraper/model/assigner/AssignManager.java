package org.scraper.model.assigner;

import org.scraper.model.Pool;
import org.scraper.model.Proxy;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Site;

import java.io.IOException;
import java.util.List;

public class AssignManager {
	
	private ScrapersFactory scrapersFactory;
	
	private ProxyChecker checker;
	
	private List<Proxy> proxy;
	
	private boolean check = false;
	
	public AssignManager(ScrapersFactory scrapersFactory, ProxyChecker checker) {
		this.scrapersFactory = scrapersFactory;
		this.checker = checker;
		check = true;
	}
	
	public AssignManager(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	public AssignManager(int size, int timeout) {
		this.scrapersFactory = new ScrapersFactory(size);
		this.checker = new ProxyChecker(new Pool(size), timeout);
		if (timeout > 0)
			check = true;
	}
	
	public List<Proxy> assign(Site site) throws InterruptedException, IOException {
		
		Assigner assigner;
		if (check) {
			assigner = new NonCheckAssigner(scrapersFactory);
		} else {
			assigner = new CheckingAssigner(scrapersFactory, checker);
		}
		
		site.setType(assigner.getType(site));
		
		proxy = assigner.getProxy();
		
		return proxy;
	}
	
	/*
	private boolean alreadyExists(Site site) {
		ScrapeType type;
		Domain siteDomain = site.getDomain();
		List<Domain> domains = globals.getDataBase().getAllDomains();
		
		if (domains.contains(siteDomain)) {
			type = domains.get(domains.indexOf(siteDomain)).getType();
			site.setType(type);
			return true;
		}
		return false;
	}*/
	
	
}
