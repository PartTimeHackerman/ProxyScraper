package org.scraper.control.filters;

import org.scraper.model.Proxy;

import java.util.Collection;
import java.util.stream.Collectors;


public class TypeFilter implements IFilter<Proxy.Type, Proxy> {
	
	private Proxy.Type type = Proxy.Type.ALL;
	
	@Override
	public Collection<Proxy> filterList(Collection<Proxy> listToFilter) {
		return listToFilter.stream()
				.filter(proxy -> proxy.getType() == type || type == Proxy.Type.ALL)
				.collect(Collectors.toList());
	}
	
	@Override
	public void setFilter(Proxy.Type filter) {
		type = filter;
	}
}
