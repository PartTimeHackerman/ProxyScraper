package org.scraper.main.manager;

import org.scraper.main.scraper.ocr.OCR;
import org.scraper.main.web.Browser;

public class QueuesManager {
	
	private Queue<Browser> browserQueue;
	
	private Queue<OCR> ocrQueue;
	
	private Integer browsers = 1, ocrs = 1;
	
	public QueuesManager(Integer browsers, Integer ocrs) {
		this.browsers = browsers;
		this.ocrs = ocrs;
		//create();
	}
	
	public void setBrowsers(Integer browsers) {
		this.browsers = browsers;
		browserQueue.setSize(browsers);
	}
	
	public void setOcrs(Integer ocrs) {
		this.ocrs = ocrs;
		ocrQueue.setSize(ocrs);
	}
	
	public void create() {
		browserQueue = new BrowserQueue(browsers);
		ocrQueue = new OcrQueue(ocrs);
	}
	
	public void shutdown() {
		browserQueue.shutdown();
		ocrQueue.shutdown();
	}
	
	public Queue<Browser> getBrowserQueue() {
		return browserQueue;
	}
	
	public Queue<OCR> getOcrQueue() {
		return ocrQueue;
	}
}
