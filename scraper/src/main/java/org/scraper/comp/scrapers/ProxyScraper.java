package org.scraper.comp.scrapers;

//import com.sun.org.apache.xpath.internal.operations.String;

import org.opencv.core.Core;
import org.scraper.comp.Main;
import org.scraper.comp.Proxy;
import org.scraper.comp.checker.ProxyChecker;
import org.scraper.comp.web.Site;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ProxyScraper {
	
	ScrapersFactory scrapersFactory;
	
	public static void main(String... args) throws Exception {
		/*PropertyConfigurator.configure("log4j.properties");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ProxyScraper ps = new ProxyScraper(new ProxyChecker(10000));
		//ProxyChecker pc = new ProxyChecker();
		//ps.ocrScraper("https://www.torvpn.com/en/proxy-list");
		//ps.normalScrape("http://proxylist.hidemyass.com/");
		//checker.checkProxy("84.238.81.21:10200", 10000);
		List<Proxy> prxs = ps.scrape(new Site("https://www.torvpn.com/en/proxy-list", ScrapeType.OCR));
		//WebDriver driver = Browser.getBrowser(null, BrowserVersion.random(), "p");
		//ps.cssScrape("http://proxylist.hidemyass.com/", driver);*/
	}
	
	
	public ProxyScraper(ScrapersFactory scrapersFactory){
		this.scrapersFactory = scrapersFactory;
	}
	
	public List<Proxy> scrape(Site site) {
		String url = site.getAddress();
		try {
			Scraper scraper = scrapersFactory.get(site.getType());
			List<Proxy> proxy;
			
			proxy = scraper.scrape(site);
			if (proxy.size() == 0)
				site.setType(ScrapeType.BLACK);
			
			return proxy;
			
		} catch (IOException e) {
			Main.log.error("Scraping failed, url: {} error: {}", url, (e.getMessage() != null ? e.getMessage() : "null"));
			return new ArrayList<>();
		} catch (InterruptedException e) {
			Main.log.error("Scraping failed, url: {} error: {}", url, (e.getMessage() != null ? e.getMessage() : "null"));
			return new ArrayList<>();
		}
	}
	
}
