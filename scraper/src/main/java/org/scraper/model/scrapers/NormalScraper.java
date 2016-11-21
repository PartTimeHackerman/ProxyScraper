package org.scraper.model.scrapers;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.scraper.model.MainLogger;
import org.scraper.model.Proxy;
import org.scraper.model.web.BrowserVersion;
import org.scraper.model.web.Site;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

class NormalScraper extends Scraper{
	
	NormalScraper(){
		type = ScrapeType.NORMAL;
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		String url = site.getAddress();
		MainLogger.log().info("Normal scraping {}", url);
		
		Document doc;
		try {
			doc = Jsoup
					.connect(url)
					.timeout(10000)
					.userAgent(BrowserVersion.random().ua())
					.get();
		} catch (IOException e) {
			MainLogger.log().info("Normal scraping {} failed!", url);
			return proxy;
		}
		
		doc.select("br").append("||newline||");
		doc.select("p").prepend("||newline||");
		doc.select("tr").prepend("||newline||");
		
		String txt = doc
				.getElementsByTag("body")
				.stream()
				.map(Element::text)
				.collect(Collectors.joining(""));
		
		proxy = matcher.match(txt);
		return proxy;
	}
}
