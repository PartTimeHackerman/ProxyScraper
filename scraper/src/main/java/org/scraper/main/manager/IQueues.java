package org.scraper.main.manager;

import org.scraper.main.scraper.ocr.OCR;
import org.scraper.main.web.Browser;

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
