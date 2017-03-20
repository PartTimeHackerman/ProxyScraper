package org.scraper.main.filters;

import org.scraper.main.data.Site;

import java.util.Collection;
import java.util.stream.Collectors;

public class AvgProxiesFilter implements IFilter<Integer, Site> {
	
	private Integer minAvg = 0;
	
	@Override
	public Collection<Site> filterList(Collection<Site> listToFilter) {
		return listToFilter.stream()
				.filter(site -> site.getAvgProxies() >= minAvg)
				.collect(Collectors.toList());
	}
	
	@Override
	public void setFilter(Integer filter) {
		minAvg = filter;
	}
}
