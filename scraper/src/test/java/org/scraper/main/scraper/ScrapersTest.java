package org.scraper.main.scraper;

import org.scraper.main.data.Site;

public class ScrapersTest {
	
	private final String testUrl = "http://absolutelydisgusting.ml/proxyTest.html";
	protected final Site testSite = new Site(testUrl, ScrapeType.NORMAL);
	
	private final String testUrlReversed = "http://absolutelydisgusting.ml/proxyTestReversed.html";
	protected final Site testSiteReversed = new Site(testUrlReversed, ScrapeType.NORMAL);
	
	protected final String firstProxy = "111.111.111.111:1111";
	
}