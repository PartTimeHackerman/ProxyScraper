package org.scraper.model.scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.scraper.model.Main;
import org.scraper.model.Pool;
import org.scraper.model.Proxy;
import org.scraper.model.web.Site;
import org.scraper.model.web.BrowserVersion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.opencv.imgproc.Imgproc.*;

public class OcrScraper extends Scraper {
	
	private Pool pool;
	
	private BlockingQueue<OCR> ocrs;
	
	public OcrScraper(Pool pool, BlockingQueue<OCR> ocrs){
		type = ScrapeType.OCR;
		this.pool=pool;
		this.ocrs = ocrs;
	}
	
	@Override
	public List<Proxy> scrape(Site site) throws InterruptedException, IOException {
		String url = site.getAddress();
		Main.log.info("OCR scraping {}", url);
		Document doc = Jsoup.connect(url).timeout(10000).userAgent(BrowserVersion.random().ua()).get();
		
		
		String mainUrl = doc.baseUri().substring(0, doc.baseUri().indexOf("/", 8));
		List<String> imgsUrls = new ArrayList<>();
		
		Elements imgs = doc.getElementsByTag("img");
		
		for (Element e : imgs) {
			if (! imgsUrls.contains(e.attr("src")))
				imgsUrls.add(e.attr("src"));
		}
		
		Main.log.info("Starting OCR");
		List<Callable<String>> calls = new ArrayList<>();
		
		for (String iurl : imgsUrls) {
			calls.add(() -> {
				byte[] imgBytes = Jsoup
						.connect(iurl.charAt(0) == '/' ? mainUrl + iurl : iurl)
						.timeout(10000)
						.userAgent(BrowserVersion.random().ua())
						.ignoreContentType(true)
						.execute()
						.bodyAsBytes();
				
				Mat mat = Imgcodecs.imdecode(new MatOfByte(imgBytes), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
				if (mat.width() / mat.height() >= 8) {
					ocrFilter(mat);
					
					OCR ocr = ocrs.take();
					String read = ocr.read(mat);
					ocrs.put(ocr);
					Main.log.info(read);
					for (Element element : imgs) {
						if (element.attr("src").equals(iurl))
							element.append(read);
					}
				}
				return null;
			});
		}
		pool.sendTasks(calls);
		
		Main.log.info("OCR Done");
		
		String txt = doc.text();
		
		proxy = RegexMatcher.match(txt);
		return proxy;
	}
	
	private void ocrFilter(Mat image) {
		double sizeMult = 300 / (double) image.height();
		resize(image, image, new Size((int) (image.size().width * sizeMult), (int) (image.size().height * sizeMult)));
		image.convertTo(image, 0, 2, - 255);
		adaptiveThreshold(image, image, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 255, 1);
	}
}
