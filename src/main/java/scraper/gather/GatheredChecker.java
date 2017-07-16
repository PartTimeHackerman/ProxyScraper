package scraper.gather;

import scraper.data.Site;
import scraper.scraper.ProxyScraper;

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
