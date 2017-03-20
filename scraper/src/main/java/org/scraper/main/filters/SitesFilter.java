package org.scraper.main.filters;

import org.scraper.main.data.Site;

import java.util.Collection;

public class SitesFilter {
	
	private IFilter<Integer, Site> avgProxiesFilter = new AvgProxiesFilter();
	private IFilter<Integer, Site> avgWorkingProxiesFilter = new AvgWorkingProxiesFilter();
	private FiltersHandler<Site> filtersHandler = new FiltersHandler<>(avgProxiesFilter, avgWorkingProxiesFilter);
	
	public Collection<Site> filterAvgProxies(Integer minSites, Collection<Site> sites) {
		avgProxiesFilter.setFilter(minSites);
		return filtersHandler.filterList(sites);
	}
	
	public Collection<Site> filterAvgWorking(Integer minWorking, Collection<Site> sites) {
		avgWorkingProxiesFilter.setFilter(minWorking);
		return filtersHandler.filterList(sites);
	}
	
}
