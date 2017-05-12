package scraper.assigner;

import scraper.Proxy;
import scraper.scraper.ScrapeType;
import scraper.data.Site;

import java.util.List;

public interface IAssigner {
	
	ScrapeType getType(Site address);
	
	List<Proxy> getProxy();
}
