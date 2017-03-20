package org.scraper.main;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.StreamPumper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.net.UrlChecker;
import org.scraper.main.data.Domain;
import org.scraper.main.data.Site;
import org.scraper.main.filters.SitesFilter;
import org.scraper.main.manager.QueuesManager;
import org.scraper.main.scraper.ScrapeType;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
		
		scraper.getSitesUtility().scrape(sitesList);
		
		Thread.sleep(20000);
		
		scraper.pause();
		
		Thread.sleep(30000);
		
		scraper.resume();
		
		Thread.sleep(10000);
		
		
		scraper.dispose();
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