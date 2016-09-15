package org.scraper.comp.scrapers;

//import com.sun.org.apache.xpath.internal.operations.String;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.scraper.comp.Globals;
import org.scraper.comp.checker.ProxyChecker;

import java.io.*;
import java.util.List;


public class ProxyScraper {
	
	private static ProxyChecker checker = new ProxyChecker(10000);
	
	private static Scraper normalScraper = new NormalScrape();
	
	private static Scraper cssScraper = new CssScraper();
	
	private static Scraper ocrScraper = new OcrScraper();

	private boolean checkOnFly;

	private static final Logger log = LogManager.getLogger(ProxyScraper.class.getSimpleName());

	public static void main(String... args) throws Exception {
		//PropertyConfigurator.configure("log4j.properties");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Globals.init(100, 3000, true);
		ProxyScraper ps = new ProxyScraper(true);
		//ProxyChecker pc = new ProxyChecker();
		//ps.ocrScraper("https://www.torvpn.com/en/proxy-list");
		//ps.normalScrape("http://proxylist.hidemyass.com/");
		//checker.checkProxy("84.238.81.21:10200", 10000);
		List<String> prxs = ps.scrape(ScrapeType.OCR,"https://www.torvpn.com/en/proxy-list");
		//WebDriver driver = Browser.getBrowser(null, BrowserVersion.random(), "p");
		//ps.cssScrape("http://proxylist.hidemyass.com/", driver);
	}

	public ProxyScraper(boolean checkOnFly) {
		this.checkOnFly = checkOnFly;
	}
	
	public List<String> scrape(ScrapeType type, String url) throws IOException, InterruptedException {
		
		switch (type){
			case NORMAL: return checkOnFly ? normalScraper.scrape(url, checker) : normalScraper.scrape(url);
			case CSS: return checkOnFly ? cssScraper.scrape(url, checker) : cssScraper.scrape(url);
			case OCR: return checkOnFly ? ocrScraper.scrape(url, checker) : ocrScraper.scrape(url);
			default: return null;
		}
	}
	
}
