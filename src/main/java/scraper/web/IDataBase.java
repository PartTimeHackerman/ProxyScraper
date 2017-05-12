package scraper.web;

import scraper.data.Domain;
import scraper.data.Site;

import java.util.List;

public interface IDataBase {
	void getSitesAndDomains();
	
	void postNew();
	
	void postSitesAndDomains();
	
	void postSites(List<Site> sites);
	
	void postDomains(List<Domain> domains);
	
	List<Site> getAllSites();
	
	List<Domain> getAllDomains();
	
	void addSite(Site site);
	
	void addDomain(Domain domain);
}
