package org.scraper.model.assigner;

import org.scraper.model.scrapers.Scraper;
import org.scraper.model.web.Site;

public interface IScrapeMethodFinder {
	Scraper findBest(Site site);
}
