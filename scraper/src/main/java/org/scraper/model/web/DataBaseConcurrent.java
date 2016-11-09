package org.scraper.model.web;

import org.scraper.model.IConcurrent;

import java.util.List;

public class DataBaseConcurrent extends DataBase implements IConcurrent {
	
	@Override
	protected void getSites(List<Site> sites) {
		send(() -> super.getSites(sites));
	}
	
	@Override
	protected void getDomains(List<Domain> domains) {
		send(() -> super.getDomains(domains));
	}
	
	@Override
	public void postSites(List<Site> sites) {
		send(() -> super.postSites(sites));
	}
	
	@Override
	protected void postDomains(List<Domain> domains) {
		send(() -> super.postDomains(domains));
	}
	
}
