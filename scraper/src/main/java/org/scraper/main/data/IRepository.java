package org.scraper.main.data;

import java.util.Collection;
import java.util.List;

public interface IRepository<T> {
	
	List<T> getAll();
	
	void add(T t);
	
	void addAll(Collection<T> ts);
	
	void fetchAllFromDB();
	
	void postOne(T t);
	
	void postAllToDB();
}
