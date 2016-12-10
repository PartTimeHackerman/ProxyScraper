package org.scraper.main.gather;

import org.scraper.main.IConcurrent;
import org.scraper.main.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class LinkGatherConcurrent extends LinksGather implements IConcurrent {
	
	public LinkGatherConcurrent(){
		super();
	}
	
	public LinkGatherConcurrent(Integer depth){
		super(depth);
	}
	
	public List<Site> gather(Site site, Boolean wait) {
		return send(() -> super.gather(site), wait);
	}
	
	@Override
	public List<Site> gatherList(List<Site> sites) {
		
		List<Callable<List<Site>>> calls = new ArrayList<>();
		List<List<Site>> proxyList;
		
		sites.forEach(site ->
							 calls.add(() ->
											   gather(site)));
		proxyList = sendTasks(calls);
		
		List<Site> proxy = new ArrayList<>();
		
		proxyList.forEach(proxy::addAll);
		
		return proxy;
	}
}
