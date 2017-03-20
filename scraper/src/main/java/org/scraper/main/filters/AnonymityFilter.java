package org.scraper.main.filters;

import org.scraper.main.Proxy;

import java.util.Collection;
import java.util.stream.Collectors;

public class AnonymityFilter implements IFilter<Proxy.Anonymity, Proxy> {
	
	private Proxy.Anonymity anonymity = Proxy.Anonymity.ALL;
	
	@Override
	public Collection<Proxy> filterList(Collection<Proxy> listToFilter) {
		return listToFilter.stream()
				.filter(proxy -> proxy.getAnonymity() == anonymity || anonymity == Proxy.Anonymity.ALL)
				.collect(Collectors.toList());
	}
	
	@Override
	public void setFilter(Proxy.Anonymity filter) {
		anonymity = filter;
	}
	
}
