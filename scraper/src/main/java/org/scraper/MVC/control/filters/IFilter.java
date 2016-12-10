package org.scraper.MVC.control.filters;

import java.util.Collection;

public interface IFilter<F, E> {
	
	Collection<E> filterList(Collection<E> listToFilter);
	
	void setFilter(F filter);
}
