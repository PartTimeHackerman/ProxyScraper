package org.scraper.main.scraper.ocr;


import org.bytedeco.javacpp.tesseract;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.scraper.main.TempFileManager;

public class OCR {
	
	private TessBaseAPI api = new TessBaseAPI();
	
	private IOcrFilter filter = new MarvinFilter();
	
	static {
		TempFileManager.loadResource(OCR.class, "tessdata/eng.traineddata");
	}
	
	public OCR() {
		if (api.Init("", "eng", tesseract.OEM_DEFAULT) != 0) {
			System.err.println("Could not initialize tesseract.");
		}
		api.SetVariable("m_data_sub_dir", System.getProperty("user.dir") + "\\");
		api.SetVariable("tessedit_char_whitelist", "0123456789.:");
		api.SetVariable("tessedit_char_blacklist", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmopqrstuvwxyz?!$^()[]{};><‘'\"\\|");
		
	}
	
	public String read(byte[] image) {
		Image filtered = filter.filter(image);
		api.SetImage(filtered.getBytes(), filtered.getWidth(), filtered.getHeight(), filtered.getBytesPerPixel(), filtered.getBytesPerLine());
		
		String text = api.GetUTF8Text()
				.getString()
				.replace(" ", "")
				.replace("\n", "");
		return text;
		
	}
	
	public void shutdown() {
		api.End();
	}
	
	public void setFilter(org.scraper.main.scraper.ocr.IOcrFilter filter) {
		this.filter = filter;
	}
}