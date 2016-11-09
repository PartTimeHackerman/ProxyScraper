package org.scraper.model.modles;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.scraper.model.*;
import org.scraper.model.managers.AssignManagerConcurrent;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.checker.ProxyCheckerConcurrent;
import org.scraper.model.gather.LinkGatherConcurrent;
import org.scraper.model.managers.*;
import org.scraper.model.scrapers.ProxyScraperConcurrent;
import org.scraper.model.web.ConnectionChecker;
import org.scraper.model.checker.IProxyChecker;
import org.scraper.model.scrapers.ProxyScraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.gather.LinksGather;
import org.scraper.model.web.DataBaseConcurrent;
import org.scraper.model.web.IDataBase;

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
	
	public static final Logger log = LogManager.getLogger(MainModel.class.getSimpleName());
	
	
	public MainModel(int threads, Integer timeout, Integer limit, Boolean check) {
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("SHUTDOWN HOOK!!!");
			dataBase.postAll();
			QueuesManager.getInstance().shutdownAll();
		}));
		
		if(!ConnectionChecker.hasConnection())
			MainModel.log.fatal("NO INTERNET CONNECTION!");
		
		MainPool.setThreadsStatic(threads);
		
		QueuesManager.setBrowsers(threads / 10);
		QueuesManager.setOcrs(threads / 10);
		
		setCheckOnFly(check);
		
		dataBase = new DataBaseConcurrent();
		dataBase.getAll();
		
		
		proxyManager = new ProxyManager(limit);
		
		sitesManager = new SitesManager(dataBase);
		
		ip = ConnectionChecker.getIp();
		
		scrapersFactory = new ScrapersFactory();
		
		
		checker = new ProxyCheckerConcurrent(timeout, getProxyManager().getAll());
		
		assigner = new AssignManagerConcurrent(scrapersFactory, checker, checkOnFly, dataBase.getAllDomains());
		
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
		MainModel.log.info("Initialized");
	}
	
	public MainModel() {
		this(100, 3000, 0, false);
	}
	
	public void setCheckOnFly(boolean checkOnFly) {
		this.checkOnFly.set(checkOnFly);
	}
	
	//@Override
	public void save() {
		
	}
	
	//@Override
	public void load() {
		
	}
	
	public void setGatherDepth(Integer depth) {
		gather.setDepth(depth);
	}
	
	public Boolean isCheckOnFly() {
		return checkOnFly.get();
	}
	
	public GlobalObserver getObserver() {
		return observer;
	}
	
	public String getIp() {
		return ip;
	}
	
	public IDataBase getDataBase() {
		return dataBase;
	}
	
	public ScrapersFactory getScrapersFactory() {
		return scrapersFactory;
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
	
	public void setSitesManager(SitesManager sitesManager) {
		this.sitesManager = sitesManager;
	}
	
	public LinksGather getGather() {
		return gather;
	}
	
	public void setVarsInterval() {
		Interval.addFunc("threads", () -> MainPool.getInstance().getThreads() - MainPool.getInstance().getActiveThreads());
		Interval.addFunc("threadsMax", () -> MainPool.getInstance().getThreads());
		
		Interval.addFunc("browsers", () -> QueuesManager.getInstance().getBrowserQueue().remainingCapacity());
		Interval.addFunc("browsersMax", () -> QueuesManager.getInstance().getBrowserQueue().getSize());
		
		Interval.addFunc("ocrs", () -> QueuesManager.getInstance().getOcrQueue().remainingCapacity());
		Interval.addFunc("ocrsMax", () -> QueuesManager.getInstance().getOcrQueue().getSize());
	}
	
	public java.util.concurrent.atomic.AtomicBoolean getCheckOnFly() {
		return checkOnFly;
	}
}
