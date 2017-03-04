package org.scraper.MVC.model;


import org.scraper.main.GlobalObserver;
import org.scraper.main.Interval;
import org.scraper.main.MainLogger;
import org.scraper.main.MainPool;
import org.scraper.main.checker.IProxyChecker;
import org.scraper.main.checker.ProxyChecker;
import org.scraper.main.checker.ProxyCheckerConcurrent;
import org.scraper.main.gather.LinkGatherConcurrent;
import org.scraper.main.gather.LinksGather;
import org.scraper.main.manager.AssignManager;
import org.scraper.main.manager.ProxyManager;
import org.scraper.main.manager.QueuesManager;
import org.scraper.main.manager.SitesManager;
import org.scraper.main.scraper.ProxyScraper;
import org.scraper.main.scraper.ProxyScraperConcurrent;
import org.scraper.main.scraper.ScrapersFactory;
import org.scraper.main.web.ConnectionChecker;
import org.scraper.main.web.DataBaseConcurrent;
import org.scraper.main.web.IDataBase;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainModel {
	
	private IDataBase dataBase;
	
	private ProxyManager proxyManager;
	
	private SitesManager sitesManager;
	
	private GlobalObserver observer;
	
	private ScrapersFactory scrapersFactory;
	
	private ProxyChecker checker;
	
	private ProxyScraper scraper;
	
	private AssignManager assigner;
	
	private LinksGather gather;
	
	private AtomicBoolean checkOnFly = new AtomicBoolean(false);
	
	private String ip;
	
	public MainModel(int threads, Integer timeout, Integer limit, Boolean check) {
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			MainLogger.log().info("SHUTDOWN HOOK!!!");
			dataBase.postAll();
			QueuesManager.getInstance().shutdownAll();
		}));
		
		if(!ConnectionChecker.hasConnection())
			MainLogger.log().fatal("NO INTERNET CONNECTION!");
		
		MainPool.setThreadsStatic(threads);
		
		QueuesManager.setBrowsers(threads / 10);
		QueuesManager.setOcrs(threads / 10 / 2);
		
		setCheckOnFly(check);
		
		dataBase = new DataBaseConcurrent();
		dataBase.getAll();
		
		proxyManager = new ProxyManager(limit);
		sitesManager = new SitesManager(dataBase);
		
		ip = ConnectionChecker.getIp();
		
		scrapersFactory = new ScrapersFactory();
		
		checker = new ProxyCheckerConcurrent(timeout, getProxyManager().getAll());
		
		assigner = new AssignManager(scrapersFactory, checker, checkOnFly, dataBase.getAllDomains());
		
		scraper = new ProxyScraperConcurrent(scrapersFactory);
		scraper.setAssigner(assigner);
		
		gather = new LinkGatherConcurrent();
		
		observer = GlobalObserver.getInstance();
		observer.setAssignManager(assigner);
		observer.setCheckOnFly(checkOnFly);
		observer.setProxyChceker(checker);
		observer.setSitesManager(sitesManager);
		observer.setProxyManager(proxyManager);
		
		checker.addObserver(observer);
		scraper.addObserver(observer);
		assigner.addObserver(observer);
		gather.addObserver(observer);
		
		synchronized (MainPool.getInstance()) {
			while (!(MainPool.getInstance().getExecutor().getActiveCount() == 0))
				MainPool.getInstance().notify();
			
		}
		MainLogger.log().info("Initialized");
	}
	
	public MainModel() {
		this(100, 3000, 0, false);
	}
	
	public void setVarsInterval() {
		Interval.addFunc("threads", () -> MainPool.getInstance().getThreads() - MainPool.getInstance().getActiveThreads());
		Interval.addFunc("threadsMax", () -> MainPool.getInstance().getThreads());
		
		Interval.addFunc("browsers", () -> QueuesManager.getInstance().getBrowserQueue().getSize());
		Interval.addFunc("browsersMax", () -> QueuesManager.getInstance().getBrowserQueue().getMaxSize());
		
		Interval.addFunc("ocrs", () -> QueuesManager.getInstance().getOcrQueue().getSize());
		Interval.addFunc("ocrsMax", () -> QueuesManager.getInstance().getOcrQueue().getMaxSize());
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
	
	public ProxyManager getProxyManager() {
		return proxyManager;
	}
	
	public AssignManager getAssigner() {
		return assigner;
	}
	
	public SitesManager getSitesManager() {
		return sitesManager;
	}
	
	public LinksGather getGather() {
		return gather;
	}
	
	public AtomicBoolean getCheckOnFly() {
		return checkOnFly;
	}
}
