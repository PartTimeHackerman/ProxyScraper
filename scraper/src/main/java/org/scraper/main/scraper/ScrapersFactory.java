package org.scraper.main.scraper;

import org.scraper.main.manager.QueuesManager;
import org.scraper.main.web.ConcurrentConnectionExecutor;

import java.util.ArrayList;
import java.util.List;

public class ScrapersFactory {
	
	private QueuesManager queuesManager;
	
	private ConcurrentConnectionExecutor concurrentConnectionExecutor;
	
	public ScrapersFactory(QueuesManager queuesManager, ConcurrentConnectionExecutor concurrentConnectionExecutor) {
		this.queuesManager = queuesManager;
		this.concurrentConnectionExecutor = concurrentConnectionExecutor;
	}
	
	
	public ScraperAbstract get(ScrapeType type) {
		switch (type) {
			case NORMAL:
				return new NormalScraper();
			case CSS:
				return new CssScraperWQueue(queuesManager);
			case OCR:
				return new OcrScraperConcurrentWQueue(concurrentConnectionExecutor, queuesManager);
			case BLACK:
				return new NullScraper();
			default:
				return new NullScraper();
		}
	}
	
	public List<ScraperAbstract> getAll() {
		List<ScraperAbstract> scrapers = new ArrayList<>();
		
		for (ScrapeType type : ScrapeType.values()) {
			if (type == ScrapeType.UNCHECKED) break;
			scrapers.add(get(type));
		}
		return scrapers;
	}
}
