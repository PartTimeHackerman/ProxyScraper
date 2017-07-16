package scraper.assigner;

import scraper.Proxy;
import scraper.data.Site;
import scraper.scraper.ScrapeType;

import java.util.List;

public interface IScrapeMethodFinder {
	
	
	ScrapeType findBest(Site site);
	
	List<Proxy> getList();
	
}
