package org.scraper.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.scraper.model.checker.ConnectionChecker;
import org.scraper.model.scrapers.ProxyScraper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Main {
	
	
	public static final Logger log = LogManager.getLogger(Main.class.getSimpleName());
	
	public static Globals globals;
	
	public static void main(String... args) {
		
		if (ConnectionChecker.hasConnection()) globals = new Globals(100, 10000, Integer.MAX_VALUE, true, false);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("SHUTDOWN HOOK!!!");
			globals.getDataBase().postAll();
		}));
		
		ProxyScraper proxyScraper = new ProxyScraper(globals.getScrapersFactory());
		
		//Site site = new Site("http://proxylist.hidemyass.com/", ScrapeType.UNCHECKED);
		//proxyScraper.scrape(site);
		WebDriver d = globals.getBrowsers().get(0).getBrowser();
		d.get("http://sh.st/CsYjH");
		((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
		
		d.get("http://aruljohn.com/details.php");
		((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
		
		globals.getDataBase().getLinks().add("http://aruljohn.com/details.php");
		globals.getDataBase().getLinks().forEach(link ->
			globals.getLinksManager().click(link, new Proxy("",80), globals.getBrowsers().get(0)));
		
		List<Callable<List<Proxy>>> calls = new ArrayList<>();
		
		
		globals.getDataBase().getAllSites().forEach(e -> calls.add(() -> proxyScraper.scrape(e)));
		
		try {
			globals.getSitesPool().sendTasks(calls);
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
		
		
		log.info(globals.getAllPrx().size());
		globals.getDataBase().postAll();
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
