package org.scraper.main.gather;

import org.scraper.main.scraper.ProxyScraper;
import org.scraper.main.web.Site;

import java.util.List;
import java.util.stream.Collectors;

public class GatheredChecker {
	
	private ProxyScraper scraper;
	
	public GatheredChecker(ProxyScraper scraper) {
		this.scraper = scraper;
	}
	
	public List<Site> check(List<Site> sites) {
		List<Site> checked = sites.stream()
				.map(site ->
							 scraper.scrape(site).size() > 0 ? site : null)
				.filter(site -> site != null)
				.collect(Collectors.toList());

		return checked;
	}
	
}
