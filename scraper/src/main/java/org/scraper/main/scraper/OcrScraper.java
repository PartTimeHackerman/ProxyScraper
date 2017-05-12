package org.scraper.main.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.scraper.main.MainLogger;
import org.scraper.main.Proxy;
import org.scraper.main.scraper.ocr.Image;
import org.scraper.main.scraper.ocr.OCR;
import org.scraper.main.web.BrowserVersion;
import org.scraper.main.data.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OcrScraper extends ScraperAbstract {
	
	private String siteRoot;
	
	private Document document;
	
	{
		type = ScrapeType.OCR;
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		siteRoot = site.getRoot();
		String url = site.getAddress();
		
		MainLogger.log(this).info("OCR scraping {}", url);
		try {
			document = Jsoup.connect(url)
					.timeout(10000)
					.userAgent(BrowserVersion.random().ua())
					.get();
		} catch (IOException e) {
			MainLogger.log(this).info("OCR scraping {} failed!", url);
			return proxy;
		}
		
		List<String> imgsUrls = getImagesUrls();
		List<Connection> connections = getConnections(imgsUrls);
		List<Connection.Response> responses = getResponses(connections);
		
		ocrAndReplace(responses);
		
		MainLogger.log(this).info("OCR Done");
		
		String txt = document.text();
		
		proxy = matcher.match(txt);
		return proxy;
	}
	
	protected List<String> getImagesUrls() {
		List<String> imgsUrls = new ArrayList<>();
		
		Elements imgs = document.getElementsByTag("img");
		
		imgs.stream()
				.map(e -> e.attr("src"))
				.distinct()
				.filter(url -> !imgsUrls.contains(url))
				//.filter(url -> url.contains(siteRoot))
				.forEach(imgsUrls::add);
		
		return imgsUrls;
	}
	
	protected List<Connection> getConnections(List<String> imgsUrls) {
		return imgsUrls.stream()
				.map(iurl ->
							 Jsoup.connect(getImageUrl(iurl))
									 .timeout(10000)
									 .userAgent(BrowserVersion.random().ua())
									 .ignoreContentType(true)
					).collect(Collectors.toList());
	}
	
	protected List<Connection.Response> getResponses(List<Connection> connections) {
		
		List<Connection.Response> responses = connections.stream()
				.map(connection -> {
					try {
						return connection.execute();
					} catch (IOException e) {
						return null;
					}
				}).collect(Collectors.toList());
		
		responses.removeIf(Objects::isNull);
		return responses;
	}
	
	protected void ocrAndReplace(List<Connection.Response> responses) {
		//responses.forEach(response -> {
		Function<Connection.Response, Connection.Response> replace = response -> {
			byte[] bytes = response.bodyAsBytes();
			Image image = new Image(bytes);
			//Assume it's proxy image
			if (image.getHeight() > 0 && image.getWidth() / image.getHeight() >= 8) {
				
				String imgText = doOcr(bytes);
				MainLogger.log(this).info(imgText);
				
				String responseUrl = response.url().toString();
				
				//Append reconized text
				document.getElementsByTag("img")
						.stream()
						.filter(element -> responseUrl.contains(element.attr("src")))
						.forEach(element -> element.append(imgText));
			}
			return null;
		};//);
		responses.forEach(response -> replace.apply(response));
	}
	
	private String getImageUrl(String url){
		return url.contains("://") || url.contains("www.")
				? url
				: url.charAt(0) == '/'
					? siteRoot + url
					: siteRoot + "/" + url;
	}
	
	protected String doOcr(byte[] image) {
		OCR ocr = new OCR();
		
		String imgText = ocr.read(image);
		
		ocr.shutdown();
		return imgText;
	}
}
