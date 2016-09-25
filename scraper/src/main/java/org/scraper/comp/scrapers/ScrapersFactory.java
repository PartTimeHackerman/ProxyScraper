package org.scraper.comp.scrapers;

import org.scraper.comp.Globals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ScrapersFactory {
	
	private Globals globals;
	
	private BlockingQueue<OCR> ocrs;
	
	public ScrapersFactory(Globals globals) {
		this.globals = globals;
		ocrs = new ArrayBlockingQueue<>(10);
	}
	
	public Scraper get(ScrapeType type) {
		switch (type) {
			case NORMAL:
				return new NormalScraper();
			case CSS:
				return new CssScraper(globals.getBrowsersQueue());
			case OCR:
				return new OcrScraper(globals.getGlobalPool(), globals.getOcrQueue());
			case UNCHECKED:
				return new UncheckedScraper();
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
