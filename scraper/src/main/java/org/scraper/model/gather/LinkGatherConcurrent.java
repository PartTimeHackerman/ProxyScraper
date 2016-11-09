package org.scraper.model.gather;

import org.scraper.model.IConcurrent;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class LinkGatherConcurrent extends LinksGather implements IConcurrent {
	
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
