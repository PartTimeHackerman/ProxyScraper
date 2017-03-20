package org.scraper.main.scraper;

import org.jsoup.Connection;
import org.scraper.main.manager.QueuesManager;
import org.scraper.main.scraper.ocr.OCR;
import org.scraper.main.web.ConcurrentConnectionExecutor;

import java.util.List;

public class OcrScraperConcurrentWQueue extends OcrScraper {
	
	private ConcurrentConnectionExecutor concurrentConnectionExecutor;
	
	private QueuesManager queuesManager;
	
	public OcrScraperConcurrentWQueue(ConcurrentConnectionExecutor concurrentConnectionExecutor, QueuesManager queuesManager) {
		this.concurrentConnectionExecutor = concurrentConnectionExecutor;
		this.queuesManager = queuesManager;
	}
	
	@Override
	protected List<Connection.Response> getResponses(List<Connection> connections) {
		return concurrentConnectionExecutor.execute(connections);
	}
	
	@Override
	protected String doOcr(byte[] image) {
		OCR ocr = queuesManager
				.getOcrQueue()
				.take();
		
		String imgText = ocr.read(image);
		
		queuesManager
				.getOcrQueue()
				.put(ocr);
		return imgText;
	}
}
