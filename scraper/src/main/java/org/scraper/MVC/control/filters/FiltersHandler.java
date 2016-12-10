package org.scraper.MVC.control.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FiltersHandler<T> {
	
	List<IFilter> filters = new ArrayList<>();
	
	public FiltersHandler(IFilter... filters){
		this.filters.addAll(Arrays.asList(filters));
	}
	
	public Collection<T> filterList(Collection<T> listToFilter){
		Collection<T> filtered = new ArrayList<>(listToFilter);
		for(IFilter filter : filters){
			filtered = filter.filterList(filtered);
		}
		return filtered;
	}
	
	public void addFilter(IFilter filter){
		filters.add(filter);
	}
}
