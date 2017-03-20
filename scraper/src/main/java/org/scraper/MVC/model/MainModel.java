package org.scraper.MVC.model;

import org.scraper.main.Interval;
import org.scraper.main.MainLogger;
import org.scraper.main.Pool;
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
import org.scraper.main.web.ConnectionChecker;
import org.scraper.main.web.IDataBase;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainModel {
	
	private DomainsRepo domainsRepo;
	
	private IDataBase dataBase;
	
	private ProxyRepo proxyRepo;
	
	private SitesRepo sitesRepo;
	
	private ScrapersFactory scrapersFactory;
	
	private ProxyChecker checker;
	
	private ProxyScraper scraper;
	
	private AssignManager assigner;
	
	private LinksGather gather;
	
	private AtomicBoolean checkOnFly = new AtomicBoolean(false);
	
	private String ip;
	private Limiter limiter;
	private Pool pool;
	
	public MainModel(int threads, Integer timeout, Integer limit, Boolean check) {
		
/*
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			MainLogger.log().info("SHUTDOWN HOOK!!!");
			dataBase.postSitesAndDomains();
			QueuesManager.getInstance().shutdown();
		}));
		
		if(!ConnectionChecker.hasConnection())
			MainLogger.log().fatal("NO INTERNET CONNECTION!");
		
		Pool.setDefaultThreads(threads);
		
		QueuesManager.setBrowsers(threads / 10);
		QueuesManager.setOcrs(threads / 10 / 2);
		
		setCheckOnFly(check);
		
		proxyRepo = new ProxyRepo(null);
		sitesRepo = new SitesRepo();
		domainsRepo = new DomainsRepo();
		
		dataBase = new DataBaseLocal(sitesRepo, domainsRepo);
		dataBase.getSitesAndDomains();
		
		ip = ConnectionChecker.getIp();
		
		ScrapersFactory scrapersFactory = new ScrapersFactory();
		
		checker = new ProxyCheckerConcurrent(timeout, getProxyRepo().getAll());
		
		assigner = new AssignManagerConcurrent(scrapersFactory, checker, checkOnFly, domainsRepo);
		
		scraper = new ProxyScraperConcurrent(scrapersFactory);
		scraper.setAssigner(assigner);
		
		gather = new LinkGatherConcurrent(sitesRepo,1);
		
		GlobalObserver observer = GlobalObserver.getInstance();
		observer.setAssignManager(assigner);
		observer.setCheckOnFly(checkOnFly);
		observer.setProxyChceker(checker);
		observer.setSitesRepo(sitesRepo);
		observer.setProxyRepo(proxyRepo);
		
		checker.addObserver(observer);
		scraper.addObserver(observer);
		assigner.addObserver(observer);
		gather.addObserver(observer);
		
		synchronized (Pool.getInstance()) {
			while (!(Pool.getInstance().getExecutor().getActiveCount() == 0))
				Pool.getInstance().notify();
			
		}
		MainLogger.log().info("Initialized");
*/
	}
	
	public MainModel() {
		this(100, 3000, 0, false);
	}
	
	public void setVarsInterval() {
		/*Interval.start();
		Interval.addFunc("threads", () -> Pool.getInstance().getThreads() - Pool.getInstance().getActiveThreads());
		Interval.addFunc("threadsMax", () -> Pool.getInstance().getThreads());
		
		Interval.addFunc("browsers", () -> QueuesManager.getInstance().getBrowserQueue().getSize());
		Interval.addFunc("browsersMax", () -> QueuesManager.getInstance().getBrowserQueue().getMaxSize());
		
		Interval.addFunc("ocrs", () -> QueuesManager.getInstance().getOcrQueue().getSize());
		Interval.addFunc("ocrsMax", () -> QueuesManager.getInstance().getOcrQueue().getMaxSize());*/
	}
	
	public void setCheckOnFly(boolean checkOnFly) {
		this.checkOnFly.set(checkOnFly);
	}
	
	public IDataBase getDataBase() {
		return dataBase;
	}
	
	public IProxyChecker getChecker() {
		return checker;
	}
	
	public ProxyScraper getScraper() {
		return scraper;
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
	
	public LinksGather getGather() {
		return gather;
	}
	
	public AtomicBoolean getCheckOnFly() {
		return checkOnFly;
	}
	
	public Limiter getLimiter() {
		return limiter;
	}
	
	public Pool getPool() {
		return pool;
	}
}
