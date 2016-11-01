package org.scraper.model.managers;

import org.scraper.model.Main;
import org.scraper.model.Pool;
import org.scraper.model.scrapers.OCR;
import org.scraper.model.web.Browser;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class QueuesManager {
	
	private Pool pool;
	
	
	private int browsersNumber;
	
	private BlockingQueue<Browser> browsersQueue;
	
	
	private int ocrNumber;
	
	private BlockingQueue<OCR> ocrQueue;
	
	public QueuesManager(Pool pool, int browsersNumber, int ocrNumber){
		
		this.pool = pool;
		setBrowsersNumber(browsersNumber);
		setOcrNumber(ocrNumber);
		
		browsersQueue = new ArrayBlockingQueue<>(100);
		ocrQueue = new ArrayBlockingQueue<>(ocrNumber);
		
		makeBrowsers();
		makeOCRs();
	}
	
	public void makeBrowsers(){
		Callable<?> makeBrowsers = () -> {
			Browser browser = new Browser();
			browsersQueue.put(browser);
			return null;
		};
		
		try {
			pool.sendTask(makeBrowsers, false, browsersNumber);
			
		} catch (Exception e) {
			Main.log.error("Failed to load browsers");
		}
	}
	
	public void makeOCRs(){
		Callable<?> makeOCR = () -> {
			OCR ocr = new OCR();
			ocrQueue.put(ocr);
			return null;
		};
		
		try {
			pool.sendTask(makeOCR, false, ocrNumber);
		} catch (Exception e) {
			Main.log.error("Failed to load OCR");
		}
	}
	
	public BlockingQueue<Browser> getBrowsersQueue() {
		return browsersQueue;
	}
	
	public BlockingQueue<OCR> getOcrQueue() {
		return ocrQueue;
	}
	
	public void setBrowsersNumber(int browsersNumber) {
		this.browsersNumber = browsersNumber <= 0 ? 1 : browsersNumber;
	}
	
	public void setOcrNumber(int ocrNumber) {
		this.ocrNumber = ocrNumber <= 0 ? 1 : ocrNumber;
	}
	
	public int getBrowsersNumber() {
		return browsersNumber;
	}
	
	public int getOcrNumber() {
		return ocrNumber;
	}
	
	public void shutdownBrowsers() {
		for (int i = 0; i < browsersQueue.size()-1 ; i++) {
				pool.sendTask(() -> {
					try {
						browsersQueue.take().shutdown();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}, false);
		}
		try {
			browsersQueue.take().shutdown();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
