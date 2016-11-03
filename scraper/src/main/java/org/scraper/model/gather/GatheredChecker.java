package org.scraper.model.gather;

import org.scraper.model.scrapers.ProxyScraper;
import org.scraper.model.web.Site;

import java.util.List;
import java.util.stream.Collectors;

public class GatheredChecker {
	
	private ProxyScraper scraper;
	
	public GatheredChecker(ProxyScraper scraper) {
		this.scraper = scraper;
	}
	
	public GatheredChecker(int size) {
		scraper = new ProxyScraper(size);
	}
	
	public List<Site> check(List<Site> sites) {
		List<Site> checked = sites.stream()
				.map(site ->
							 scraper.scrapeConcurrent(site, false).size() > 0 ? site : null)
				.filter(site -> site != null)
				.collect(Collectors.toList());

		return checked;
	}
	
}
