package org.scraper.model;

import org.scraper.model.assigner.AssignManager;
import org.scraper.model.scraper.ProxyScraper;
import org.scraper.model.scraper.ScrapeType;
import org.scraper.model.web.Site;

import java.util.List;
import java.util.stream.Collectors;

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
		List<Site> sites = mainModel.getTextManager().getSites().stream()
				.filter(site -> site.getType()== ScrapeType.UNCHECKED)
				.collect(Collectors.toList());
		
		assigner.assignList(sites);
		
		scraper.scrapeList(sites);
	}
	
	@Override
	public void check() {
		assigner.assignList(mainModel.getTextManager().getSites());
	}
	
	@Override
	public void crawl() {
		
	}
}
