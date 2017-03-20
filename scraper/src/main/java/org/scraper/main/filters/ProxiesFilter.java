package org.scraper.main.filters;

import org.scraper.main.Proxy;

import java.util.Collection;

public class ProxiesFilter {
	
	protected IFilter<Proxy.Anonymity, Proxy> anonymityFilter = new AnonymityFilter();
	protected IFilter<Proxy.Type, Proxy> typeFilter = new TypeFilter();
	protected IFilter<Float, Proxy> timeFilter = new TimeFilter();
	protected IFilter<Boolean, Proxy> workingFilter = new WorkingFilter();
	protected FiltersHandler<Proxy> filtersHandler = new FiltersHandler<>(anonymityFilter, typeFilter, timeFilter, workingFilter);
	
	public Collection<Proxy> filterByType(Proxy.Type type, Collection<Proxy> proxies) {
		typeFilter.setFilter(type);
		return filtersHandler.filterList(proxies);
	}
	
	public Collection<Proxy> filterByAnonymity(Proxy.Anonymity anonymity, Collection<Proxy> proxies) {
		anonymityFilter.setFilter(anonymity);
		return filtersHandler.filterList(proxies);
	}
	
	public Collection<Proxy> filterByTimeout(Float time, Collection<Proxy> proxies) {
		timeFilter.setFilter(time);
		return filtersHandler.filterList(proxies);
	}
	
	public Collection<Proxy> filterBroken(boolean broken, Collection<Proxy> proxies) {
		workingFilter.setFilter(broken);
		return filtersHandler.filterList(proxies);
	}
}
