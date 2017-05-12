package scraper.web;

import scraper.IConcurrent;
import scraper.Pool;
import scraper.data.Domain;
import scraper.data.Site;

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
