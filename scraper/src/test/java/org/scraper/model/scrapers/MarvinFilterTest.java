package org.scraper.model.scrapers;

import org.junit.Test;
import org.scraper.model.scrapers.ocr.IOcrFilter;
import org.scraper.model.scrapers.ocr.MarvinFilter;
import org.scraper.model.scrapers.ocr.OCR;

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