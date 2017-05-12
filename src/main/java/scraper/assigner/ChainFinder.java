package scraper.assigner;

import scraper.Proxy;
import scraper.scraper.ScrapeType;
import scraper.scraper.ScraperAbstract;
import scraper.scraper.ScrapersFactory;
import scraper.data.Site;

import java.util.ArrayList;
import java.util.List;

public class ChainFinder implements IScrapeMethodFinder {
	
	private ScrapersFactory scrapersFactory;
	
	private List<Proxy> proxies = new ArrayList<>();
	
	private Integer minimumProxies = 10;
	
	public ChainFinder(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	@Override
	public ScrapeType findBest(Site site) {
		proxies.clear();
		if (site.getType() != ScrapeType.UNCHECKED
				&& site.getType() != ScrapeType.BLACK) {
			ScraperAbstract scraper = scrapersFactory.get(site.getType());
			scraper.scrape(site);
			proxies = scraper.getScraped();
			return scraper.getType();
		}
		
		List<ScraperAbstract> scrapers = scrapersFactory.getAll();
		
		for (ScraperAbstract scraper : scrapers) {
			if (scraper.scrape(site).size() >= minimumProxies) {
				proxies = scraper.getScraped();
				return scraper.getType();
			}
		}
		return ScrapeType.BLACK;
	}
	
	@Override
	public List<Proxy> getList() {
		return proxies;
	}
}
