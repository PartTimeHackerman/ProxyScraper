package org.scraper.model;

import org.scraper.model.scraper.ScrapeType;
import org.scraper.model.web.DataBase;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SitesManager {
	
	private List<Site> all = Collections.synchronizedList(new ArrayList<>());
	
	private List<Site> working = Collections.synchronizedList(new ArrayList<>());
	
	public SitesManager(DataBase dataBase){
		all.addAll(dataBase.getAllSites());
	}
	
	public void addSite(Site site) {
		if (!all.contains(site)) {
			all.add(site);
			if (site.getType() != ScrapeType.UNCHECKED) working.add(site);
		}
	}
	
	public void addSites(List<Site> all) {
		all.forEach(this::addSite);
	}
	
	public Site getIfPresent(Site site){
		return (all.contains(site)) ? all.get(all.indexOf(site)) : site;
	}
}
