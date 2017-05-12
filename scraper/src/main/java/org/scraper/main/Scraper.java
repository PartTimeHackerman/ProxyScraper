package org.scraper.main;

import org.scraper.MVC.model.ProxyUtility;
import org.scraper.MVC.model.SitesUtility;
import org.scraper.main.checker.IProxyChecker;
import org.scraper.main.checker.ProxyChecker;
import org.scraper.main.checker.ProxyCheckerConcurrent;
import org.scraper.main.data.DataBaseLocal;
import org.scraper.main.data.DomainsRepo;
import org.scraper.main.data.ProxyRepo;
import org.scraper.main.data.SitesRepo;
import org.scraper.main.gather.LinkGatherConcurrent;
import org.scraper.main.gather.LinksGather;
import org.scraper.main.limiter.Limiter;
import org.scraper.main.manager.*;
import org.scraper.main.scraper.ProxyScraper;
import org.scraper.main.scraper.ProxyScraperConcurrent;
import org.scraper.main.scraper.ScrapersFactory;
import org.scraper.main.web.ConcurrentConnectionExecutor;
import org.scraper.main.web.ConnectionChecker;
import org.scraper.main.web.IDataBase;

import java.util.concurrent.atomic.AtomicBoolean;

public class Scraper {
	
	private final IDataBase dataBase;
	private final ProxyRepo proxyRepo;
	private final SitesRepo sitesRepo;
	private final DomainsRepo domainsRepo;
	        
	private final ProxyChecker proxyChecker;
	private final ProxyScraper proxyScraper;
	        
	private final AssignManager assigner;
	private final LinksGather linksGather;
	        
	private final ProxyUtility proxyUtility;
	private final SitesUtility sitesUtility;
	
	private final ProxiesObserver proxiesObserver;
	private final SitesObserver sitesObserver;
	
	private final QueuesManager queuesManager;
	private final ConcurrentConnectionExecutor concurrentConnectionExecutor;
	
	private final Pool pool;
	
	private final Limiter limiter;
	        
	private final AtomicBoolean checkOnFly = new AtomicBoolean(false);
	private final String ip;
	
	private Boolean created;
	private Boolean paused;
	private Integer limit;
	
	public Scraper(Integer threads, Integer timeout, Integer limit, Boolean check, Integer browsers, Integer ocrs) {
		
		Pool.setDefaultThreads(threads);
		
		pool = new Pool(threads);
		
		this.limit = limit;
		limiter = new Limiter(limit);
		proxyRepo = new ProxyRepo(limiter);
		sitesRepo = new SitesRepo();
		domainsRepo = new DomainsRepo();
		
		dataBase = new DataBaseLocal(sitesRepo, domainsRepo);
		dataBase.getSitesAndDomains();
		
		Runtime.getRuntime().addShutdownHook(new Thread(this::dispose));
		
		if(!ConnectionChecker.hasConnection())
			MainLogger.log(this).fatal("NO INTERNET CONNECTION!");
		
		queuesManager = new QueuesManager(browsers, ocrs);
		concurrentConnectionExecutor = new ConcurrentConnectionExecutor(pool);
		
		setCheckOnFly(check);
		
		ip = ConnectionChecker.getIp();
		
		ScrapersFactory scrapersFactory = new ScrapersFactory(queuesManager, concurrentConnectionExecutor);
		
		proxyChecker = new ProxyCheckerConcurrent(timeout,proxyRepo, pool);
		
		assigner = new AssignManagerConcurrent(scrapersFactory, proxyChecker, checkOnFly, domainsRepo, pool);
		
		proxyScraper = new ProxyScraperConcurrent(scrapersFactory, pool);
		proxyScraper.setAssigner(assigner);
		
		linksGather = new LinkGatherConcurrent(sitesRepo, 2, pool);
		
		proxiesObserver = new ProxiesObserver(proxyRepo, proxyChecker, checkOnFly);
		sitesObserver = new SitesObserver(sitesRepo, assigner);
		
		proxyChecker.addObserver(proxiesObserver);
		proxyScraper.addObserver(proxiesObserver);
		assigner.addObserver(sitesObserver);
		linksGather.addObserver(sitesObserver);
		
		proxyUtility = new ProxyUtility(proxyChecker, pool);
		sitesUtility = new SitesUtility(assigner, proxyScraper, linksGather, pool);
		
		
		
		limiter.addSwitchable(proxyChecker);
		limiter.addSwitchable(proxyScraper);
		
		MainLogger.log(this).info("Scraper initialized");
		//MainLogger.getInstance().setLevel(Level.FATAL);
	}
	
	public Scraper(Integer threads, Integer timeout, Integer limit, Boolean check){
		this(threads, timeout, limit, check, threads / 10, threads / 10 / 2);
	}
	
	public void create(){
		MainLogger.log(this).debug("Creating scraper");
		pool.create();
		synchronized (pool) {
			while (!(pool.getExecutor().getActiveCount() == 0))
				pool.notify();
		}
		queuesManager.create();
		created = true;
		limiter.setLimit(limit);
	}
	
	public void pause(){
		MainLogger.log(this).debug("Pausing scraper");
		pool.pause();
		paused = true;
	}
	
	public void resume(){
		MainLogger.log(this).debug("Resuming scraper");
		pool.resume();
		paused = false;
	}
	
	public void dispose(){
		MainLogger.log(this).debug("Shutting down scraper");
		limit = limiter.getLimit();
		limiter.setLimit(0);
		pool.shutdown();
		queuesManager.shutdown();
		dataBase.postSitesAndDomains();
		created = false;
		
	}
	
	public void setCheckOnFly(boolean checkOnFly) {
		this.checkOnFly.set(checkOnFly);
	}
	
	public IProxyChecker getProxyChecker() {
		return proxyChecker;
	}
	
	public org.scraper.main.scraper.ProxyScraper getProxyScraper() {
		return proxyScraper;
	}
	
	public ProxyRepo getProxyRepo() {
		return proxyRepo;
	}
	
	public AssignManager getAssigner() {
		return assigner;
	}
	
	public SitesRepo getSitesRepo() {
		return sitesRepo;
	}
	
	public LinksGather getLinksGather() {
		return linksGather;
	}
	
	public AtomicBoolean getCheckOnFly() {
		return checkOnFly;
	}
	
	public ProxyUtility getProxyUtility() {
		return proxyUtility;
	}
	
	public SitesUtility getSitesUtility() {
		return sitesUtility;
	}
	
	public DomainsRepo getDomainsRepo() {
		return domainsRepo;
	}
	
	public Boolean isCreated() {
		return created;
	}
	
	public Boolean isPaused() {
		return paused;
	}
	
	public Pool getPool() {
		return pool;
	}
	
	public Limiter getLimiter() {
		return limiter;
	}
}
