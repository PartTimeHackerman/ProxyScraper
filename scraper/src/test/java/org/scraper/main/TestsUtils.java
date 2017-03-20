package org.scraper.main;

import org.scraper.main.assigner.BestOfAllFinder;
import org.scraper.main.assigner.IScrapeMethodFinder;
import org.scraper.main.checker.ProxyChecker;
import org.scraper.main.checker.ProxyCheckerConcurrent;
import org.scraper.main.manager.QueuesManager;
import org.scraper.main.scraper.ScrapeType;
import org.scraper.main.scraper.ScrapersFactory;
import org.scraper.main.data.Site;
import org.scraper.main.web.ConcurrentConnectionExecutor;

import java.util.ArrayList;

public class TestsUtils {
	
	public static final Site normalSite = new Site("https://incloak.com/proxy-list/", ScrapeType.NORMAL);
	public static final Site cssSite = new Site("http://proxylist.hidemyass.com/", ScrapeType.CSS);
	public static final Site ocrSite = new Site("https://www.torvpn.com/en/proxy-list", ScrapeType.OCR);
	
	public static final Proxy localProxy = new Proxy("127.0.0.1:80");
	public static final Proxy brokenProxy1 = new Proxy("111.111.111.111:1111");
	public static final Proxy brokenProxy2 = new Proxy("222.222.222.222:2222");
	public static final Proxy brokenProxy3 = new Proxy("333.333.333.333:3333");
	
	public static final ProxyChecker checker = new ProxyCheckerConcurrent(3000, new ArrayList<>(), new Pool(1));
	public static final ScrapersFactory scrapersFactory = new ScrapersFactory(new QueuesManager(1, 1), new ConcurrentConnectionExecutor(new Pool(1)));
	public static final IScrapeMethodFinder methodFinder = new BestOfAllFinder(scrapersFactory);
}
