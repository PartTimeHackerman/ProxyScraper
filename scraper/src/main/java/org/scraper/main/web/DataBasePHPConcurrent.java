package org.scraper.main.web;

import org.scraper.main.IConcurrent;
import org.scraper.main.Pool;
import org.scraper.main.data.Domain;
import org.scraper.main.data.Site;

import java.util.List;

public class DataBasePHPConcurrent extends DataBasePHP implements IConcurrent {
	
	private Pool pool;
	
	public DataBasePHPConcurrent(Pool pool) {
		this.pool = pool;
	}
	
	@Override
	protected void getSites(List<Site> sites) {
		send(() -> super.getSites(sites), pool);
	}
	
	@Override
	protected void getDomains(List<Domain> domains) {
		send(() -> super.getDomains(domains), pool);
	}
	
	@Override
	public void postSites(List<Site> sites) {
		send(() -> super.postSites(sites), pool);
	}
	
	@Override
	public void postDomains(List<Domain> domains) {
		send(() -> super.postDomains(domains), pool);
	}
	
}
