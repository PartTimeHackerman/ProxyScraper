package org.scraper.model.modles;

import org.scraper.model.assigner.AssignManager;
import org.scraper.model.scrapers.ProxyScraper;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.Site;

import java.util.List;

public class ScraperModel implements Model {
	
	private MainModel mainModel;
	
	private ProxyScraper scraper;
	
	private AssignManager assigner;
	
	private List<Site> sites;
	
	public ScraperModel(MainModel mainModel, ProxyScraper scraper, List<Site> sites) {
		this.mainModel = mainModel;
		this.scraper = scraper;
		this.sites = sites;
	}
	
	@Override
	public void scrape() {
		/*List<Site> sites = mainModel.getTextManager().getSites().stream()
				.filter(site -> site.getType()== ScrapeType.UNCHECKED)
				.collect(Collectors.toList());
		
		assigner.assignList(sites);
		
		scrapers.scrapeList(sites);*/
		scraper.scrape(new Site("https://incloak.com/proxy-list/", ScrapeType.NORMAL));
	}
	
	@Override
	public void check() {
	}
	
	@Override
	public void crawl(){
	}
}
