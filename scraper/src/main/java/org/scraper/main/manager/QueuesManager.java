package org.scraper.main.manager;

import org.scraper.main.scraper.ocr.OCR;
import org.scraper.main.web.Browser;

public class QueuesManager{
	
	private static QueuesManager queuesManager;
	
	private Queue<Browser> browserQueue;
	
	private Queue<OCR> ocrQueue;
	
	private static Integer browsers = 1, ocrs = 1;
	
	private QueuesManager(Integer browsers, Integer ocrs){
		browserQueue = new BrowserQueue(browsers);
		ocrQueue = new OcrQueue(ocrs);
	}
	
	public static QueuesManager getInstance(){
		if (queuesManager == null){
			synchronized (QueuesManager.class){
				if(queuesManager == null)
					queuesManager = new QueuesManager(browsers, ocrs);
			}
		}
		return queuesManager;
	}
	
	public static void setBrowsers(Integer browsers){
		QueuesManager.browsers = browsers;
		if(queuesManager != null)
			queuesManager.browserQueue.setSize(browsers);
	}
	
	public static void setOcrs(Integer ocrs){
		QueuesManager.ocrs = ocrs;
		if(queuesManager != null)
			queuesManager.ocrQueue.setSize(ocrs);
	}
	
	public void shutdownAll(){
		browserQueue.shutdownAll();
		ocrQueue.shutdownAll();
	}
	
	public Queue<Browser> getBrowserQueue() {
		return browserQueue;
	}
	
	public Queue<OCR> getOcrQueue() {
		return ocrQueue;
	}
}
