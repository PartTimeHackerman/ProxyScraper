package scraper.view.control;

import java.util.List;

public interface IDeleter<T> {
	
	void deleteSelected(List<T> selected);
}
