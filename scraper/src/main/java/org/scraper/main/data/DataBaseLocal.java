package org.scraper.main.data;

import org.scraper.main.MainLogger;
import org.scraper.main.web.IDataBase;

import java.util.List;

public class DataBaseLocal implements IDataBase {
	
	private SitesRepo sitesRepo;
	
	private DomainsRepo domainsRepo;
	
	public DataBaseLocal(SitesRepo sitesRepo, DomainsRepo domainsRepo){
		this.sitesRepo = sitesRepo;
		this.domainsRepo = domainsRepo;
	}
	
	@Override
	public void getSitesAndDomains() {
		sitesRepo.fetchAllFromDB();
		domainsRepo.fetchAllFromDB();
	}
	
	@Override
	public void postNew() {
		MainLogger.log().info("Not avaiable in local DB");
	}
	
	@Override
	public void postSitesAndDomains() {
		sitesRepo.postAllToDB();
		domainsRepo.postAllToDB();
	}
	
	@Override
	public void postSites(List<Site> sites) {
		sitesRepo.addAll(sites);
		sitesRepo.postAllToDB();
	}
	
	@Override
	public void postDomains(List<Domain> domains) {
		domainsRepo.addAll(domains);
		domainsRepo.postAllToDB();
	}
	
	@Override
	public List<Site> getAllSites() {
		return sitesRepo.getAll();
	}
	
	@Override
	public List<Domain> getAllDomains() {
		return domainsRepo.getAll();
	}
	
	@Override
	public void addSite(Site site) {
		sitesRepo.add(site);
	}
	
	@Override
	public void addDomain(Domain domain) {
		domainsRepo.add(domain);
	}
}
