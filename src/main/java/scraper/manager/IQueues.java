package scraper.manager;

import scraper.scraper.ocr.OCR;
import scraper.web.Browser;

public interface IQueues {
	Integer getBrowsersNumber();
	
	void setBrowsersNumber(int browsersNumber);
	
	Integer getOcrNumber();
	
	void setOcrNumber(int ocrNumber);
	
	void shutdownAll();
	
	void shutdownBrowsers();
	
	void shutdownOcrs();
	
	Browser takeBrowser();
	
	void putBrowser(Browser browser);
	
	OCR takeOCR();
	
	void putOCR(OCR ocr);
}
