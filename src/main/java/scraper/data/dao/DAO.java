package scraper.data.dao;


import java.util.List;

public interface DAO<T> {
	
	void post(T t);
	
	void postAll(List<T> sites);
	
	List<T> fetchAll();
}
