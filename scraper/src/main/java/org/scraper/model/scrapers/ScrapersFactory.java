package org.scraper.model.scrapers;

import java.util.ArrayList;
import java.util.List;

public class ScrapersFactory {
	
	public Scraper get(ScrapeType type) {
		switch (type) {
			case NORMAL:
				return new NormalScraper();
			case CSS:
				return new CssScraperWQueue();
			case OCR:
				return new OcrScraperConcurrentWQueue();
			case BLACK:
				return new NullScraper();
			default:
				return new NullScraper();
		}
	}
	
	public List<Scraper> getAll() {
		List<Scraper> scrapers = new ArrayList<>();
		
		for (ScrapeType type : ScrapeType.values()) {
			if (type == ScrapeType.UNCHECKED) break;
			scrapers.add(get(type));
		}
		return scrapers;
	}
}
