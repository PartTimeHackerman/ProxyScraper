package org.scraper.comp.scrapers;

import org.scraper.comp.checker.ProxyChecker;

import java.io.IOException;
import java.util.List;

public interface Scraper {
	public List<String> scrape(String url, ProxyChecker checker) throws InterruptedException, IOException;
	
	public List<String> scrape(String url) throws InterruptedException, IOException;
}
