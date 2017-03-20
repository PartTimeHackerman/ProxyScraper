package org.scraper.main.data;

import org.scraper.MVC.model.SitesUtility;
import org.scraper.main.data.IRepository;
import org.scraper.main.data.dao.SiteDAO;
import org.scraper.main.scraper.ScrapeType;
import org.scraper.main.data.Site;
import org.scraper.main.web.IDataBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SitesRepo implements IRepository<Site> {
	
	private final List<Site> sites = Collections.synchronizedList(new ArrayList<>());
	
	private final List<Site> working = Collections.synchronizedList(new ArrayList<>());
	
	private SitesUtility model;
	
	private SiteDAO siteDAO = new SiteDAO();
	
	public SitesRepo() {
		//addSites(siteDAO.fetchAll());
	}
	
	public void addSite(Site site) {
		if (!sites.contains(site)) {
			sites.add(site);
			if (site.getType() != ScrapeType.UNCHECKED && site.getType() != ScrapeType.BLACK)
				working.add(site);
			if (model != null)
				model.addSite(site);
		}
	}
	
	public void addSites(Collection<Site> all) {
		all.forEach(this::addSite);
	}
	
	public Site getIfPresent(Site site) {
		return (sites.contains(site)) ? sites.get(sites.indexOf(site)) : site;
	}
	
	public void setModel(SitesUtility model) {
		this.model = model;
		sites.forEach(model::addSite);
	}
	
	public List<Site> getAll() {
		return sites;
	}
	
	public boolean contains(String siteAddress) {
		return sites.stream().anyMatch(site ->
											   site.getAddress().equals(siteAddress));
	}
	
	@Override
	public void add(Site site) {
		addSite(site);
	}
	
	@Override
	public void addAll(Collection<Site> sites) {
		addSites(sites);
	}
	
	@Override
	public void fetchAllFromDB() {
		addSites(siteDAO.fetchAll());
	}
	
	@Override
	public void postOne(Site site) {
		siteDAO.post(site);
	}
	
	@Override
	public void postAllToDB() {
		siteDAO.postAll(sites);
	}
	
	public List<Site> getWorking() {
		return working;
	}
}
