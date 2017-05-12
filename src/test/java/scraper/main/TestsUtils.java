package scraper.main;

import scraper.Pool;
import scraper.Proxy;
import scraper.assigner.BestOfAllFinder;
import scraper.assigner.IScrapeMethodFinder;
import scraper.checker.ProxyChecker;
import scraper.checker.ProxyCheckerConcurrent;
import scraper.data.ProxyRepo;
import scraper.limiter.Limiter;
import scraper.manager.QueuesManager;
import scraper.scraper.ScrapeType;
import scraper.scraper.ScrapersFactory;
import scraper.data.Site;
import scraper.web.ConcurrentConnectionExecutor;

public class TestsUtils {
	
	public static final Site normalSite = new Site("https://incloak.com/proxy-list/", ScrapeType.NORMAL);
	public static final Site cssSite = new Site("http://proxylist.hidemyass.com/", ScrapeType.CSS);
	public static final Site ocrSite = new Site("https://www.torvpn.com/en/proxy-list", ScrapeType.OCR);
	
	public static final Proxy localProxy = new Proxy("127.0.0.1:80");
	public static final Proxy brokenProxy1 = new Proxy("111.111.111.111:1111");
	public static final Proxy brokenProxy2 = new Proxy("222.222.222.222:2222");
	public static final Proxy brokenProxy3 = new Proxy("333.333.333.333:3333");
	
	public static final ProxyChecker checker = new ProxyCheckerConcurrent(3000, new ProxyRepo(new Limiter(1)), new Pool(1));
	public static final ScrapersFactory scrapersFactory = new ScrapersFactory(new QueuesManager(1, 1), new ConcurrentConnectionExecutor(new Pool(1)));
	public static final IScrapeMethodFinder methodFinder = new BestOfAllFinder(scrapersFactory);
}
