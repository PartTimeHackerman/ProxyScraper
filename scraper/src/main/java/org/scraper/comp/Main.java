package org.scraper.comp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.scraper.comp.checker.ConnectionChecker;
import org.scraper.comp.checker.ProxyChecker;
import org.scraper.comp.scrapers.ProxyScraper;
import org.scraper.comp.web.DataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Main {
	
	
	public static final Logger log = LogManager.getLogger(Main.class.getSimpleName());
	
	public static void main(String... args) {
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("SHUTDOWN HOOK!!!");
			DataBase.postAll();
		}));
		
		
		if (ConnectionChecker.hasConnection()) Globals.init(100, 10000, Integer.MAX_VALUE, true, false);
		
		ProxyScraper proxyScraper = new ProxyScraper(new ProxyChecker(Globals.getTimeout()));
		
		//Site site = new Site("http://proxylist.hidemyass.com/", ScrapeType.UNCHECKED);
		//proxyScraper.scrape(site);
		WebDriver d = Globals.getBrowsers().get(0).getBrowser();
		d.get("http://sh.st/CsYjH");
		((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
		
		d.get("http://aruljohn.com/details.php");
		((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
		
		DataBase.getLinks().add("http://aruljohn.com/details.php");
		DataBase.getLinks().forEach(link ->
			Globals.getLinksManager().click(link, new Proxy("",80), Globals.getBrowsers().get(0)));
		
		List<Callable<List<Proxy>>> calls = new ArrayList<>();
		
		
		DataBase.getAllSites().forEach(e -> calls.add(() -> proxyScraper.scrape(e)));
		
		try {
			Globals.sitesPool.sendTasks(calls);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/*try {
			Pool.forEach(DataBase.getAllSites(), site -> {
				try {
					proxyScraper.scrape(site);
				} catch (InterruptedException e) {
					log.warn("Thread interrupted {}", e);
				}
				return null;
			});
		} catch (InterruptedException e) {
			log.warn("Thread interrupted {}", e);
		}*/
		
		
		log.info(Globals.getAllPrx().size());
		DataBase.postAll();
		//IntStream.range(0, 10)
		//Globals.getPool().awaitTermination(10, TimeUnit.SECONDS);

		/*synchronized(Pool.getPool()) {
			while (Pool.getPool().getActiveCount() == 0)
				Pool.getPool().wait(); //wait for the queue to become empty
		}

		log.info(Globals.getBrowsers().size());
		Globals.getBrowsers()
				.parallelStream()
				.forEach(browser -> browser.getBrowser().quit());
		log.info(Globals.getBrowsers().size());*/
		
	}
}
