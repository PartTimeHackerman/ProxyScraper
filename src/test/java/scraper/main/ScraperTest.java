package scraper.main;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scraper.MainLogger;
import scraper.Scraper;
import scraper.data.Site;
import scraper.filters.SitesFilter;

import java.util.*;

public class ScraperTest {
	
	private static Scraper scraper;
	
	@BeforeClass
	public static void setUp() {
		scraper = new Scraper(50, 10000, Integer.MAX_VALUE, true, 5, 2);
	}
	
	@Test
	public void test() throws Exception {
		
		scraper.create();
		
		SitesFilter sitesFilter = new SitesFilter();
		Collection<Site> sites = scraper.getSitesRepo().getAll();
		
		sites = scraper.getSitesUtility().getSitesFilter().filterAvgWorking(5, sites);
		
		List<Site> sitesList = new ArrayList<>(sites);
		sitesList.sort((o1, o2) -> o2.getAvgWorkingProxies() - o1.getAvgWorkingProxies());
		Collections.reverse(sitesList);
		scraper.getSitesUtility().scrape(sitesList.get(0));
		
		scraper.getProxyRepo().getWorkingStream().subscribe(p -> MainLogger.log(this).info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!{}", p));
		
		Thread.sleep(60000);
		Thread.sleep(60000);
		Thread.sleep(60000);
		scraper.getSitesUtility().scrape(sitesList.get(1));
		Thread.sleep(6000000);
		/*
		scraper.pause();
		
		Thread.sleep(10000);
		
		scraper.resume();
		
		Thread.sleep(10000);*/
		
		
		scraper.dispose();
		Thread.sleep(10000);
		/*sites = sites.stream().filter(site -> site.getType() == ScrapeType.UNCHECKED).collect(Collectors.toList());
		List<Domain> domains = scraper.getDomainsRepo().getAll();
		
		sites = sites.stream().filter(site -> {
			Domain domain = site.getDomain();
			if (domains.indexOf(domain) == -1) return false;
			domain = domains.get(domains.indexOf(domain));
			return domain.getType() != ScrapeType.BLACK && domain.getType() != ScrapeType.UNCHECKED;
		}).collect(Collectors.toList());*/
		
		
		//QueuesManager.getInstance();
		
		//MainPool.getInstance();
		
		//MainPool.getInstance().shutdown();
		
		//scraper.dispose();
		
		//System.in.read();
		//assertTrue(!scraper.getProxyRepo().getAll().isEmpty());
	}
	
	@AfterClass
	public static void tearDown() {
		scraper.dispose();
	}
	
}