package org.scraper.MVC.control.filters;

import org.scraper.main.web.Site;

import java.util.Collection;
import java.util.stream.Collectors;

public class AvgWorkingFilter implements IFilter<Integer, Site> {
	
	private Integer minAvg = 0;
	
	@Override
	public Collection<Site> filterList(Collection<Site> listToFilter) {
		return listToFilter.stream()
				.filter(site -> site.getAvgWorking() >= minAvg)
				.collect(Collectors.toList());
	}
	
	@Override
	public void setFilter(Integer filter) {
		minAvg = filter;
	}
}
