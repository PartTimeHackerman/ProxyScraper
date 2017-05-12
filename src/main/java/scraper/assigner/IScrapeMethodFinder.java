package scraper.assigner;

import scraper.Proxy;
import scraper.scraper.ScrapeType;
import scraper.data.Site;

import java.util.List;

public interface IScrapeMethodFinder {
	
	
	ScrapeType findBest(Site site);
	
	List<Proxy> getList();
	
}
