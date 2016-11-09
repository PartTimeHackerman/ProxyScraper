package org.scraper.model.managers;

import org.scraper.model.scrapers.OCR;
import org.scraper.model.web.Browser;

public interface IQueues {
	void setBrowsersNumber(int browsersNumber);
	
	void setOcrNumber(int ocrNumber);
	
	Integer getBrowsersNumber();
	
	Integer getOcrNumber();
	
	void shutdownAll();
	
	void shutdownBrowsers();
	
	void shutdownOcrs();
	
	Browser takeBrowser();
	
	void putBrowser(Browser browser);
	
	OCR takeOCR();
	
	void putOCR(OCR ocr);
}
