package org.scraper.comp.web;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.lept.PIX;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bytedeco.javacpp.lept.pixDestroy;
import static org.bytedeco.javacpp.lept.pixRead;
import static org.openqa.selenium.By.tagName;

public class Main {

	public static void main(String[] args) throws Exception {

		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\geckodriver.exe");

		Document doc = Jsoup.connect("http://www.samair.ru/proxy/").timeout(10000).userAgent(BrowserVersion.FIREFOX_MAC.ua()).get();
		String txt = doc.tagName("body").text();

		WebDriver drv = null;//Browser.getBrowser(null, BrowserVersion.FIREFOX_UB, "c");
		drv.get("http://www.samair.ru/proxy/");
		String txt2 = drv.findElement(tagName("body")).getText();


		Long counter = System.currentTimeMillis();

		WebDriver augmentedDriver = new Augmenter().augment(drv);
		File ss = ((TakesScreenshot) augmentedDriver).
				getScreenshotAs(OutputType.FILE);



		BytePointer outText;

		TessBaseAPI api = new TessBaseAPI();
		// Initialize tesseract-ocr with English, without specifying tessdata path
		if (api.Init(null, "eng") != 0) {
			System.err.println("Could not initialize tesseract.");
		}

		// Open input image with leptonica library
		PIX image = pixRead(ss.getAbsolutePath());
		api.SetImage(image);
		// Get OCR result
		outText = api.GetUTF8Text();
		System.out.println("OCR output:\n" + outText.getString());

		// Destroy used object and release memory
		api.End();
		outText.deallocate();
		pixDestroy(image);


		counter = System.currentTimeMillis() - counter;

		Pattern ipRegex = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
		Pattern portRegex = Pattern.compile("[0-9]{1,4}");

		Matcher ipMatcher = ipRegex.matcher(txt);
		Matcher portMatcher = portRegex.matcher(txt);

		List<String> proxy = new ArrayList<>();
		String tempProxy;

		while (ipMatcher.find()) {
			tempProxy = "";
			tempProxy += ipMatcher.group() + ":";

			if (portMatcher.find(ipMatcher.end())) {
				tempProxy += portMatcher.group();
				proxy.add(tempProxy);
			}
		}


		int brwsrs = 10;
		ExecutorService executor = Executors.newFixedThreadPool(brwsrs);

		CountDownLatch latch = new CountDownLatch(brwsrs);
		for (int i = 0; i < brwsrs; i++) {

			int finalI = i;
			executor.execute(() -> {
				WebDriver driver = null;//Browser.getBrowser(null, BrowserVersion.FIREFOX_UB, "p");
				driver.get("http://www.google.com");//"http://proxylist.hidemyass.com/#listable");
				System.out.println("GOT" + finalI);
				latch.countDown();
			});
		}
		latch.await();


		//String allVisible = driver.findElement(By.tagName("body")).getText();


		System.exit(0);
	}
}
