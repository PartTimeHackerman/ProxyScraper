package org.scraper.comp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scraper.comp.scrapers.ProxyScraper;
import org.scraper.comp.scrapers.ScrapeType;
import org.scraper.comp.web.Address;
import org.scraper.comp.scrapers.SitesScraper;

public class Main {


	public static final Logger log = LogManager.getLogger(Main.class.getSimpleName());

	public static void main(String... args) throws Exception {

		Globals.init(100, 1000, true);

		SitesScraper sitesScraper = new SitesScraper();

		try {
			log.info(sitesScraper.getType(new Address("http://proxylist.hidemyass.com/", Address.Type.UNCHECKED, "")));
		} catch (InterruptedException e) {
			log.warn("Thread interrupted {}", e);
		}

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
