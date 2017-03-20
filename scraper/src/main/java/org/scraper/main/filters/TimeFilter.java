package org.scraper.main.filters;

import org.scraper.main.Proxy;

import java.util.Collection;
import java.util.stream.Collectors;

public class TimeFilter implements IFilter<Float, Proxy> {
	
	private Float time = Float.MAX_VALUE;
	
	@Override
	public Collection<Proxy> filterList(Collection<Proxy> listToFilter) {
		return listToFilter.stream()
				.filter(proxy -> proxy.getSpeed() <= time)
				.collect(Collectors.toList());
	}
	
	@Override
	public void setFilter(Float filter) {
		time = filter > 0 ? filter : Float.MAX_VALUE;
	}
}
