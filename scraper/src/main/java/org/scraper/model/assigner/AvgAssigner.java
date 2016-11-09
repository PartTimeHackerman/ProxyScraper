package org.scraper.model.assigner;

import org.scraper.model.Proxy;
import org.scraper.model.checker.IProxyChecker;
import org.scraper.model.web.Site;

import java.util.List;
import java.util.stream.Collectors;

public class AvgAssigner {
	
	public static void assignAvg(Site site, List<Proxy> proxies, IProxyChecker checker){
		assignAvgProxies(site, proxies.size());
		
		if (checker != null){
			Double woking = workingPrecent(proxies, checker);
			assignAvgWorking(site,woking);
		}
	}
	
	public static void assignAvg(Site site, List<Proxy> proxies){
		assignAvg(site, proxies, null);
	}
	
	private static void assignAvgProxies(Site site, Integer proxies){
		site.setAvgSites(site.getAvgSites() == null
								 ? (site.getAvgSites() + proxies) / 2
								 : proxies);
	}
	
	private static void assignAvgWorking(Site site, Double workingPrecent){
		Integer avgSites = site.getAvgSites();
		Integer avgWorking = site.getAvgWorking();
		
		avgWorking = avgWorking > 0
				? ((avgWorking + (int) (avgSites * workingPrecent)) / 2)
				: (int) (avgSites * workingPrecent);
		
		site.setAvgWorking(avgWorking);
	}
	
	private static double workingPrecent(List<Proxy> proxies, IProxyChecker checker) {
		int all = proxies.size();
		int workingSize = checker.checkProxies(proxies)
				.stream()
				.filter(Proxy::isWorking)
				.collect(Collectors.toList())
				.size();
		
		return workingSize / (double) all;
	}
}
