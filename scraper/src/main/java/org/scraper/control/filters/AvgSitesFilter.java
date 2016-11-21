package org.scraper.control.filters;

import org.scraper.model.web.Site;

import java.util.Collection;
import java.util.stream.Collectors;

public class AvgSitesFilter implements IFilter<Integer, Site> {
	
	private Integer minAvg = 0;
	
	@Override
	public Collection<Site> filterList(Collection<Site> listToFilter) {
		return listToFilter.stream()
				.filter(site -> site.getAvgSites() >= minAvg)
				.collect(Collectors.toList());
	}
	
	@Override
	public void setFilter(Integer filter) {
		minAvg = filter;
	}
}
