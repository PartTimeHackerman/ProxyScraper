package org.scraper.comp.spider;

import org.scraper.comp.Globals;
import org.scraper.comp.Main;
import org.scraper.comp.checker.ProxyChecker;
import org.scraper.comp.scrapers.ProxyScraper;
import org.scraper.comp.scrapers.ScrapeType;
import org.scraper.comp.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GatheredChecker {
	
	private ProxyScraper scraper;
	
	private List<Site> sites;
	
	private List<Site> checked = new ArrayList<>();
	
	public static void main(String[] args) {
		
		LinksGather lg = new LinksGather(new Site("http://proxylist.hidemyass.com/", ScrapeType.UNCHECKED), 2);
		lg.startGather();
		List<Site> sites = lg.getGathered();
		
		GatheredChecker gc = new GatheredChecker(sites, 3000);
		gc.check();
		sites = gc.getChecked();
		sites.forEach(site -> Main.log.info(site.getAddress()));
	}
	
	public GatheredChecker(List<Site> gathered, int timeout) {
		sites = gathered;
		ProxyChecker checker = null;
		if (timeout > 0) checker = new ProxyChecker(timeout);
		scraper = new ProxyScraper(checker);
	}
	
	public void check() {
		List<Site> checked = sites.stream()
				.map(site -> scraper.scrape(site).size() > 0 ? site : null)
				.filter(site -> site != null)
				.collect(Collectors.toList());
		
		this.checked.addAll(checked);
	}
	
	public List<Site> getChecked() {
		return checked;
	}
}
