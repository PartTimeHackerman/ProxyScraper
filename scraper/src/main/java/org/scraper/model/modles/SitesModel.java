package org.scraper.model.modles;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scraper.model.assigner.AssignManager;
import org.scraper.model.gather.LinksGather;
import org.scraper.model.scrapers.ProxyScraper;
import org.scraper.model.web.DataBase;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;

public class SitesModel {
	
	private ProxyScraper scraper;
	
	private AssignManager assigner;
	
	private LinksGather gather;
	
	private DataBase dataBase;
	
	private ObservableList<Site> visible = FXCollections.observableArrayList();
	
	public SitesModel(AssignManager assigner, ProxyScraper scraper, LinksGather gather, DataBase dataBase) {
		this.assigner = assigner;
		this.scraper = scraper;
		this.gather = gather;
		this.dataBase = dataBase;
	}
	
	public void addSite(Site site){
		Platform.runLater(() -> visible.add(site));
	}
	
	public ObservableList<Site> getVisible(){
		return visible;
	}
	
	public void scrape(List<Site> sites) {
		scraper.scrapeList(sites);
	}
	
	public void check(List<Site> sites) {
		
		assigner.assignList(sites);
		dataBase.postSites(new ArrayList<>(sites));
	}
	
	public void crawl(List<Site> sites){
		gather.gatherList(sites);
	}
}
