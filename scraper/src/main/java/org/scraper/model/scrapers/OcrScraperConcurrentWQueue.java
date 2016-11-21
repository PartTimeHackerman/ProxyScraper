package org.scraper.model.scrapers;

import org.jsoup.Connection;
import org.scraper.model.managers.QueuesManager;
import org.scraper.model.scrapers.ocr.OCR;
import org.scraper.model.web.ConcurrentConnectionExecutor;

import java.util.List;

public class OcrScraperConcurrentWQueue extends OcrScraper {
	
	@Override
	protected List<Connection.Response> getResponses(List<Connection> connections) {
		return ConcurrentConnectionExecutor.getInstance().execute(connections);
	}
	
	@Override
	protected String doOcr(byte[] image){
		OCR ocr = QueuesManager.getInstance()
				.getOcrQueue()
				.take();
		
		String imgText = ocr.read(image);
		
		QueuesManager.getInstance()
				.getOcrQueue()
				.put(ocr);
		return imgText;
	}
}
