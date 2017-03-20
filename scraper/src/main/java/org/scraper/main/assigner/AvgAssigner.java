package org.scraper.main.assigner;

import org.scraper.main.Proxy;
import org.scraper.main.checker.IProxyChecker;
import org.scraper.main.data.Site;

import java.util.List;
import java.util.stream.Collectors;

public class AvgAssigner {
	
	static void assignAvg(Site site, List<Proxy> proxies, IProxyChecker checker) {
		assignAvgProxies(site, proxies.size());
		
		if (checker != null) {
			Double woking = workingPrecent(proxies, checker);
			assignAvgWorking(site, woking);
		}
	}
	
	public static void assignAvg(Site site, List<Proxy> proxies) {
		assignAvg(site, proxies, null);
	}
	
	private static void assignAvgProxies(Site site, Integer proxies) {
		site.setAvgProxies(site.getAvgProxies() == null
								   ? (site.getAvgProxies() + proxies) / 2
								   : proxies);
	}
	
	private static void assignAvgWorking(Site site, Double workingPrecent) {
		Integer avgSites = site.getAvgProxies();
		Integer avgWorking = site.getAvgWorkingProxies();
		
		avgWorking = avgWorking > 0
				? ((avgWorking + (int) (avgSites * workingPrecent)) / 2)
				: (int) (avgSites * workingPrecent);
		
		site.setAvgWorkingProxies(avgWorking);
	}
	
	private static double workingPrecent(List<Proxy> proxies, IProxyChecker checker) {
		int all = proxies.size();
		checker.checkProxies(proxies);
		int workingSize = proxies
				.stream()
				.filter(Proxy::isWorking)
				.collect(Collectors.toList())
				.size();
		
		return workingSize / (double) all;
	}
}
