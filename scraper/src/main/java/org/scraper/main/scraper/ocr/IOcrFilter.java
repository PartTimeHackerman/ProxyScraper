package org.scraper.main.scraper.ocr;

public interface IOcrFilter {
	
	Image filter(byte[] image);
	
}
