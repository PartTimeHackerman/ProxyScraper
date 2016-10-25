package org.scraper.model.scrapers;

import org.scraper.model.Main;
import org.scraper.model.Pool;
import org.scraper.model.Proxy;
import org.scraper.model.assigner.AssignManager;
import org.scraper.model.web.Site;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


public class ProxyScraper extends Observable {
	
	private ScrapersFactory scrapersFactory;
	
	private Pool pool;
	
	private AssignManager assigner;
	
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
	
	
	public ProxyScraper(ScrapersFactory scrapersFactory, Pool pool) {
		this.scrapersFactory = scrapersFactory;
		this.pool = pool;
	}
	
	public ProxyScraper(int size) {
		this.scrapersFactory = new ScrapersFactory(size);
	}
	
	public List<Proxy> scrape(Site site) {
		String url = site.getAddress();
		List<Proxy> proxy = new ArrayList<>();
		
		pool.sendTask(() -> {
			try {
				if (assigner != null && site.getType() == ScrapeType.UNCHECKED)
					assigner.assignConcurrent(site);
				Scraper scraper = scrapersFactory.get(site.getType());
				
				
				proxy.addAll(scraper.scrape(site));
				if (proxy.size() == 0)
					site.setType(ScrapeType.BLACK);
				
				setChanged();
				notifyObservers(proxy);
				
			} catch (IOException | InterruptedException e) {
				Main.log.error("Scraping failed, url: {} error: {}", url, (e.getMessage() != null ? e.getMessage() : "null"));
				//return new ArrayList<>();
			}
			
		}, false);
		return proxy;
		
	}
	
	public List<Proxy> scrapeList(List<Site> sites) {
		
		List<Callable<List<Proxy>>> calls = new ArrayList<>();
		List<List<Proxy>> proxyList;
		
		sites.stream()
				.map(site ->
							 calls.add(() -> scrape(site)))
				.collect(Collectors.toList());
		
		proxyList = pool.sendTasks(calls);
		
		List<Proxy> proxy = new ArrayList<>();
		
		proxyList.forEach(proxy::addAll);
		
		return proxy;
	}
	
	public void setAssigner(AssignManager assigner) {
		this.assigner = assigner;
	}
	
}
