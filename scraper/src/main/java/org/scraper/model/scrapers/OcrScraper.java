package org.scraper.model.scrapers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.scraper.model.Proxy;
import org.scraper.model.managers.QueuesManager;
import org.scraper.model.modles.MainModel;
import org.scraper.model.web.BrowserVersion;
import org.scraper.model.web.ConcurrentConnectionExecutor;
import org.scraper.model.web.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.opencv.imgproc.Imgproc.*;

class OcrScraper extends Scraper {
	
	private String siteRoot;
	
	private Document document;
	
	static {
		//Load OpenCV lib
		String sysArch = System.getProperty("os.arch");
		sysArch = sysArch.substring(sysArch.length() - 2);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME + "x" + sysArch);
	}
	
	{
		type = ScrapeType.OCR;
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		siteRoot = site.getRoot();
		String url = site.getAddress();
		
		MainModel.log.info("OCR scraping {}", url);
		try {
			document = Jsoup.connect(url)
					.timeout(10000)
					.userAgent(BrowserVersion.random().ua())
					.get();
		} catch (IOException e) {
			MainModel.log.info("OCR scraping {} failed!", url);
			return proxy;
		}
		
		List<String> imgsUrls = getImagesUrls();
		List<Connection> connections = getConnections(imgsUrls);
		List<Connection.Response> responses = getResponses(connections);
		
		ocrAndReplace(responses);
		
		MainModel.log.info("OCR Done");
		
		String txt = document.text();
		
		proxy = matcher.match(txt);
		return proxy;
	}
	
	protected List<String> getImagesUrls() {
		List<String> imgsUrls = new ArrayList<>();
		
		Elements imgs = document.getElementsByTag("img");
		
		imgs.stream()
				.filter(e -> !imgsUrls.contains(e.attr("src")))
				.forEach(e -> imgsUrls.add(e.attr("src")));
		return imgsUrls;
	}
	
	protected List<Connection> getConnections(List<String> imgsUrls) {
		return imgsUrls.stream()
				.map(iurl ->
							 Jsoup.connect(iurl.charAt(0) == '/' ? siteRoot + iurl : iurl)
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
		responses.forEach(response -> {
			byte[] image = response.bodyAsBytes();
			Mat mat = Imgcodecs.imdecode(new MatOfByte(image), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
			//Assume it's proxy image
			if (mat.height() > 0 && mat.width() / mat.height() >= 8) {
				applyVisibilityFilters(mat);
				
				String imgText = doOcr(mat);
				MainModel.log.info(imgText);
				
				String responseUrl = response.url().toString();
				
				//Append reconized text
				document.getElementsByTag("img")
						.stream()
						.filter(element -> responseUrl.contains(element.attr("src")))
						.forEach(element -> element.append(imgText));
			}
		});
	}
	
	protected String doOcr(Mat mat) {
		OCR ocr = new OCR();
		
		String imgText = ocr.read(mat);
		
		ocr.shutdown();
		return imgText;
	}
	
	private void applyVisibilityFilters(Mat image) {
		double sizeMult = 300 / (double) image.height();
		resize(image, image, new Size((int) (image.size().width * sizeMult), (int) (image.size().height * sizeMult)));
		image.convertTo(image, 0, 2, -255);
		adaptiveThreshold(image, image, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 255, 1);
	}
}
