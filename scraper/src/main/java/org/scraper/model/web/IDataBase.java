package org.scraper.model.web;

import java.util.List;

public interface IDataBase {
	void getAll();
	
	void postNew();
	
	void postAll();
	
	void postSites(List<Site> sites);
	
	List<Site> getAllSites();
	
	List<Domain> getAllDomains();
	
	void addSite(Site site);
	
	void addDomain(Domain domain);
}
