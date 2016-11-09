package org.scraper.model.managers;

import org.scraper.model.modles.SitesModel;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.IDataBase;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SitesManager {
	
	
	private List<Site> all = Collections.synchronizedList(new ArrayList<>());
	
	private List<Site> working = Collections.synchronizedList(new ArrayList<>());
	
	private static String sitePattern = ".*\\.[a-z]{2,3}.*";
	
	private SitesModel model;
	
	public SitesManager(IDataBase IDataBase) {
		addSites(IDataBase.getAllSites());
	}
	
	public void addSite(Site site) {
		if (!all.contains(site)) {
			all.add(site);
			if (site.getType() != ScrapeType.UNCHECKED) working.add(site);
			if (model != null) model.addSite(site);
		}
	}
	
	public void addSite(String siteString) {
		if (!siteString.matches(sitePattern)) return;
		Site site = new Site(siteString, ScrapeType.UNCHECKED);
		if (!all.contains(site))
			addSite(site);
	}
	
	public void addSites(List<Site> all) {
		all.forEach(this::addSite);
	}
	
	public Site getIfPresent(Site site) {
		return (all.contains(site)) ? all.get(all.indexOf(site)) : site;
	}
	
	public void setModel(SitesModel model) {
		this.model = model;
		all.forEach(model::addSite);
	}
}
