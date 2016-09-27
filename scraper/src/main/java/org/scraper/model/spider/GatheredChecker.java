package org.scraper.model.spider;

import org.scraper.model.Main;
import org.scraper.model.scraper.ProxyScraper;
import org.scraper.model.scraper.ScrapeType;
import org.scraper.model.scraper.ScrapersFactory;
import org.scraper.model.web.Site;

import java.util.List;
import java.util.stream.Collectors;

public class GatheredChecker {
	
	private ProxyScraper scraper;
	
	public static void main(String[] args) {
		
		LinksGather lg = new LinksGather(2);
		List<Site> sites = lg.gather(new Site("http://proxylist.hidemyass.com/", ScrapeType.CSS));
		
		
		GatheredChecker gc = new GatheredChecker(10);
		sites = gc.check(sites);
		sites.forEach(site -> Main.log.info(site.getAddress()));
	}
	
	public GatheredChecker(ScrapersFactory scrapersFactory) {
		scraper = new ProxyScraper(scrapersFactory);
	}
	
	public GatheredChecker(int size) {
		scraper = new ProxyScraper(size);
	}
	
	public List<Site> check(List<Site> sites) {
		List<Site> checked = sites.stream()
				.map(site -> scraper.scrape(site).size() > 0 ? site : null)
				.filter(site -> site != null)
				.collect(Collectors.toList());

		return checked;
	}
	
}
