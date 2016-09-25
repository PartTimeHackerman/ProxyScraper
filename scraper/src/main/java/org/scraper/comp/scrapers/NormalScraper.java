package org.scraper.comp.scrapers;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.scraper.comp.Main;
import org.scraper.comp.Proxy;
import org.scraper.comp.web.Site;
import org.scraper.comp.web.BrowserVersion;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class NormalScraper extends Scraper{
	
	public NormalScraper(){
		type = ScrapeType.NORMAL;
	}
	
	@Override
	public List<Proxy> scrape(Site site) throws InterruptedException, IOException {
		String url = site.getAddress();
		Main.log.info("Normal scraping {}", url);
		
		Document doc = Jsoup
				.connect(url)
				.timeout(10000)
				.userAgent(BrowserVersion.random().ua())
				.get();
		
		doc.select("br").append("||newline||");
		doc.select("p").prepend("||newline||");
		doc.select("tr").prepend("||newline||");
		
		String txt = doc
				.getElementsByTag("body")
				.stream()
				.map(Element::text)
				.collect(Collectors.joining(""));
		
		proxy = RegexMatcher.match(txt);
		return proxy;
	}
}
