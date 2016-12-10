package org.scraper.MVC.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scraper.MVC.control.IClipboard;
import org.scraper.MVC.control.IDeleter;
import org.scraper.MVC.control.filters.AvgSitesFilter;
import org.scraper.MVC.control.filters.AvgWorkingFilter;
import org.scraper.MVC.control.filters.FiltersHandler;
import org.scraper.MVC.control.filters.IFilter;
import org.scraper.main.IConcurrent;
import org.scraper.main.gather.LinksGather;
import org.scraper.main.manager.AssignManager;
import org.scraper.main.scraper.ProxyScraper;
import org.scraper.main.scraper.ScrapeType;
import org.scraper.main.web.IDataBase;
import org.scraper.main.web.LanguageCheck;
import org.scraper.main.web.Site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class SitesModel implements IConcurrent, IDeleter<Site>, IClipboard<Site> {
	
	private ProxyScraper scraper;
	
	private AssignManager assigner;
	
	private LinksGather gather;
	
	private IDataBase dataBase;
	
	private ObservableList<Site> visible = FXCollections.observableArrayList();
	private List<Site> all = new ArrayList<>();
	
	private IFilter<Integer, Site> avgSitesFilter = new AvgSitesFilter();
	private IFilter<Integer, Site> avgWorkingFilter = new AvgWorkingFilter();
	private FiltersHandler<Site> filtersHandler = new FiltersHandler<>(avgSitesFilter, avgWorkingFilter);
	
	private static String sitePattern = ".*\\.[a-z]{2,3}.*";
	
	public SitesModel(AssignManager assigner, ProxyScraper scraper, LinksGather gather, IDataBase dataBase) {
		this.assigner = assigner;
		this.scraper = scraper;
		this.gather = gather;
		this.dataBase = dataBase;
	}
	
	public void addSite(Site site) {
		if (all.contains(site) || LanguageCheck.isFromOtherLang(site, all)) return;
		all.add(site);
		
		Platform.runLater(() -> visible.add(site));
	}
	
	public void addSite(String siteString) {
		if (!siteString.matches(sitePattern)) return;
		Site site = new Site(siteString, ScrapeType.UNCHECKED);
		addSite(site);
	}
	
	public ObservableList<Site> getVisible() {
		return visible;
	}
	
	public void scrape(List<Site> sites) {
		send(() ->
					 scraper.scrapeList(sites), false);
	}
	
	public void check(List<Site> sites) {
		send(() ->
			 {
				 assigner.assignList(sites);
				 //dataBase.postSites(new ArrayList<>(sites));
			 }, false);
	}
	
	public void gather(List<Site> sites, Integer depth) {
		send(() ->
			 {
				 gather.setDepth(depth);
				 gather.gatherList(sites);
			 }, false);
	}
	
	public void filterAvgSites(Integer minSites){
		avgSitesFilter.setFilter(minSites);
		Collection<Site> filtered = filtersHandler.filterList(all);
		visible.clear();
		visible.addAll(filtered);
	}
	
	public void filterAvgWorking(Integer minWorking){
		avgWorkingFilter.setFilter(minWorking);
		Collection<Site> filtered = filtersHandler.filterList(all);
		visible.clear();
		visible.addAll(filtered);
	}
	
	@Override
	public void deleteSelected(List<Site> selected) {
		for (Site site : new CopyOnWriteArrayList<>(selected)){
			visible.remove(site);}
	}
	
	@Override
	public void handleCopy(List<Site> proxyList) {
		String selectedProxiesString = proxyList.stream()
				.map(Site::getAddress)
				.collect(Collectors.joining("\n"));
		copy(selectedProxiesString);
	}
	
	@Override
	public void handlePaste() {
		List<String> clipboardContent = paste();
		clipboardContent.forEach(this::addSite);
	}
}
