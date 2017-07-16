package scraper.assigner;

import scraper.Proxy;
import scraper.data.Site;
import scraper.scraper.ScrapeType;

import java.util.List;

public interface IAssigner {
	
	ScrapeType getType(Site address);
	
	List<Proxy> getProxy();
}
