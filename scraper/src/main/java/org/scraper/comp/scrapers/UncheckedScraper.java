package org.scraper.comp.scrapers;

import org.scraper.comp.Proxy;
import org.scraper.comp.assigner.Assigner;
import org.scraper.comp.assigner.CheckingAssigner;
import org.scraper.comp.assigner.NonCheckAssigner;
import org.scraper.comp.web.Domain;
import org.scraper.comp.web.Site;
import org.scraper.comp.web.DataBase;

import java.io.IOException;
import java.util.List;

public class UncheckedScraper extends Scraper {
	
	public UncheckedScraper(){
		type = ScrapeType.UNCHECKED;
	}
	
	@Override
	public List<Proxy> scrape(Site site) throws InterruptedException, IOException {
		if (alreadyExists(site))
			return globals.scrapers()
					.getByType(site.getType())
					.scrape(site, checker);
		
		Assigner assigner;
		if (checker == null) {
			assigner = new NonCheckAssigner();
		} else {
			assigner = new CheckingAssigner(checker, new ProxyScraper(checker));
		}
		
		site.setType(assigner.getType(site));
		
		proxy = assigner.getProxy();
		
		DataBase.addDomain(site.getDomain());
		
		return proxy;
	}
	
	private boolean alreadyExists(Site site) {
		ScrapeType type;
		Domain siteDomain = site.getDomain();
		List<Domain> domains = DataBase.getAllDomains();
		
		if (domains.contains(siteDomain)) {
			type = domains.get(domains.indexOf(siteDomain)).getType();
			site.setType(type);
			return true;
		}
		return false;
	}
	
	
}
