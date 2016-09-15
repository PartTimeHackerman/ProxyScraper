package org.scraper.comp.scrapers;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.scraper.comp.Main;
import org.scraper.comp.checker.ProxyChecker;
import org.scraper.comp.web.BrowserVersion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NormalScrape implements Scraper{
	
	@Override
	public List<String> scrape(String url) throws IOException, InterruptedException {
		return scrape(url, null);
	}
	
	@Override
	public List<String> scrape(String url, ProxyChecker checker) throws InterruptedException, IOException {
		Main.log.info("Normal scraping {}", url);
		
		Document doc = Jsoup.connect(url).timeout(10000).userAgent(BrowserVersion.random().ua()).get();
		String txt = doc.tagName("body").text();
		return checker!=null ? RegexMatcher.match(txt,checker) : RegexMatcher.match(txt);
	}
}
