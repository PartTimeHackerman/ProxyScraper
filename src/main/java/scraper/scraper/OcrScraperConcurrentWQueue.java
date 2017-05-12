package scraper.scraper;

import org.jsoup.Connection;
import scraper.manager.QueuesManager;
import scraper.scraper.ocr.OCR;
import scraper.web.ConcurrentConnectionExecutor;

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
