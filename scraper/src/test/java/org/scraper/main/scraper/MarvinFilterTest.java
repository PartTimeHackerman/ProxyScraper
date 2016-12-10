package org.scraper.main.scraper;

import org.junit.Test;
import org.scraper.main.scraper.ocr.IOcrFilter;
import org.scraper.main.scraper.ocr.MarvinFilter;
import org.scraper.main.scraper.ocr.OCR;

import static org.junit.Assert.assertEquals;

public class MarvinFilterTest extends FilterTest{
	
	@Test
	public void filter() throws Exception {
		OCR ocr = new OCR();
		IOcrFilter filter = new MarvinFilter();
		ocr.setFilter(filter);
		
		String result = ocr.read(image);
		assertEquals(result, expected);
	}
	
}