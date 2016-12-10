package org.scraper.MVC.control.filters;

import org.scraper.main.Proxy;

import java.util.Collection;
import java.util.stream.Collectors;

public class WorkingFilter implements IFilter<Boolean, Proxy> {
	
	private Boolean working = false;
	
	@Override
	public Collection<Proxy> filterList(Collection<Proxy> listToFilter) {
		return listToFilter.stream()
				.filter(proxy -> proxy.isWorking() || working)
				.collect(Collectors.toList());
	}
	
	@Override
	public void setFilter(Boolean filter) {
		working = filter;
	}
}
