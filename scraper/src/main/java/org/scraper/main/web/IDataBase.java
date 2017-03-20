package org.scraper.main.web;

import org.scraper.main.data.Domain;
import org.scraper.main.data.Site;

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
