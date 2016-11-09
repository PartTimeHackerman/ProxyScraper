package org.scraper.model.scrapers;

import org.junit.Test;
import static org.junit.Assert.*;
import org.scraper.model.Proxy;
import org.scraper.model.web.Site;

import java.util.List;

public class ScrapersTest {
	
	private final String testUrl = "http://absolutelydisgusting.ml/proxyTest.html";
	protected final Site testSite = new Site(testUrl, ScrapeType.NORMAL);
	
	private final String testUrlReversed = "http://absolutelydisgusting.ml/proxyTestReversed.html";
	protected final Site testSiteReversed = new Site(testUrlReversed, ScrapeType.NORMAL);
	
	protected final String firstProxy = "111.111.111.111:1111";
	
}