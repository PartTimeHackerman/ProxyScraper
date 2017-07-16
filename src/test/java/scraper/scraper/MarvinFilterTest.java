package scraper.scraper;

import org.junit.Test;
import scraper.scraper.ocr.IOcrFilter;
import scraper.scraper.ocr.MarvinFilter;
import scraper.scraper.ocr.OCR;

import static org.junit.Assert.assertEquals;

public class MarvinFilterTest extends FilterTest {
	
	@Test
	public void filter() throws Exception {
		OCR ocr = new OCR();
		IOcrFilter filter = new MarvinFilter();
		ocr.setFilter(filter);
		
		String result = ocr.read(image);
		assertEquals(result, expected);
	}
	
}