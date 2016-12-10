package org.scraper.main.scraper;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.scraper.main.MainLogger;
import org.scraper.main.Proxy;
import org.scraper.main.web.BrowserVersion;
import org.scraper.main.web.Site;

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
