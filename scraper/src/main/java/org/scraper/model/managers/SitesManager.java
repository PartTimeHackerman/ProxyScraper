package org.scraper.model.managers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.DataBase;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.application.Platform;

public class SitesManager {
	
	private ObservableList<Site> visible = FXCollections.observableArrayList();
	
	private List<Site> all = Collections.synchronizedList(new ArrayList<>());
	
	private List<Site> working = Collections.synchronizedList(new ArrayList<>());
	
	private static String sitePattern = ".*\\.[a-z]{2,3}.*";
	
	public SitesManager(DataBase dataBase){
		all.addAll(dataBase.getAllSites());
	}
	
	public void addSite(Site site) {
		if (!all.contains(site)) {
			all.add(site);
			if (site.getType() != ScrapeType.UNCHECKED) working.add(site);
		}
		Platform.runLater(() -> visible.add(site));
	}
	
	public void addSite(String siteString){
		if(!siteString.matches(sitePattern)) return;
		Site site = new Site(siteString, ScrapeType.UNCHECKED);
		if(!all.contains(site))
			addSite(site);
	}
	
	public ObservableList<Site> getVisible(){
		return visible;
	}
	
	public void addSites(List<Site> all) {
		all.forEach(this::addSite);
	}
	
	public Site getIfPresent(Site site){
		return (all.contains(site)) ? all.get(all.indexOf(site)) : site;
	}
}
