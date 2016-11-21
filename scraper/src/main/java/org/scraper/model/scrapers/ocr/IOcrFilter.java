package org.scraper.model.scrapers.ocr;

public interface IOcrFilter {
	
	Image filter(byte[] image);
	
}
