package scraper.scraper.ocr;

public interface IOcrFilter {
	
	Image filter(byte[] image);
	
}
