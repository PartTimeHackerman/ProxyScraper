package org.scraper.model.scrapers;

import org.scraper.model.Pool;
import org.scraper.model.web.Browser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class ScrapersFactory {
	
	private Pool pool;
	
	private BlockingQueue<Browser> browsers;
	
	private BlockingQueue<OCR> ocrs;
	
	public ScrapersFactory(Pool pool, BlockingQueue<Browser> browsers, BlockingQueue<OCR> ocrs) {
		this.pool = pool;
		this.browsers = browsers;
		this.ocrs = ocrs;
	}
	
	public ScrapersFactory(int size) {
		this.pool = new Pool(size);
		this.browsers = new ArrayBlockingQueue<>(size);
		this.ocrs = new ArrayBlockingQueue<>(size);
		
		try {
			browsers.put(new Browser());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			IntStream.range(0, size).forEach(i -> {
				
				try {
					ocrs.put(new OCR());
				} catch (InterruptedException e) {}
			});
	}
	
	public Scraper get(ScrapeType type) {
		switch (type) {
			case NORMAL:
				return new NormalScraper();
			case CSS:
				return new CssScraper(browsers);
			case OCR:
				return new OcrScraper(pool, ocrs);
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
