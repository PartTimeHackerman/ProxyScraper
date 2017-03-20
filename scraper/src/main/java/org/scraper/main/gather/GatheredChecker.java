package org.scraper.main.gather;

import org.scraper.main.scraper.ProxyScraper;
import org.scraper.main.data.Site;

import java.util.List;
import java.util.Objects;
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
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		return checked;
	}
	
}
