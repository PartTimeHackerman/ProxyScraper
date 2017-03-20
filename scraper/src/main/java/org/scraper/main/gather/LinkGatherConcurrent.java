package org.scraper.main.gather;

import org.scraper.main.IConcurrent;
import org.scraper.main.Pool;
import org.scraper.main.data.Site;
import org.scraper.main.data.SitesRepo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

public class LinkGatherConcurrent extends LinksGather implements IConcurrent {
	
	private Pool pool;
	
	public LinkGatherConcurrent(SitesRepo sitesRepo, Integer depth, Pool pool) {
		super(sitesRepo, depth);
		this.pool = pool;
	}
	
	public LinkGatherConcurrent(Integer depth) {
		super(depth);
	}
	
	public List<Site> gather(Site site, Boolean wait) {
		return pool.sendTask(() -> super.gather(site), wait);
	}
	
	@Override
	public List<Site> gatherSites(Collection<Site> sites) {
		
		List<Callable<List<Site>>> calls = new ArrayList<>();
		List<List<Site>> gatheredSitesLists;
		
		sites.forEach(site ->
							  calls.add(() ->
												gather(site)));
		
		gatheredSitesLists = pool.sendTasks(calls);// sendToSubPool(sites, this::gather, MainPool.getInstance().getThreads() / 10);
		
		List<Site> gatheredSites = new ArrayList<>();
		
		gatheredSitesLists.forEach(gatheredSites::addAll);
		
		return gatheredSites;
	}
}
