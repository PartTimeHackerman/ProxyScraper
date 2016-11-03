package org.scraper.model.modles;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.scraper.model.*;
import org.scraper.model.managers.AssignManager;
import org.scraper.model.web.ConnectionChecker;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.managers.ProxyManager;
import org.scraper.model.managers.QueuesManager;
import org.scraper.model.managers.SitesManager;
import org.scraper.model.scrapers.ProxyScraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.gather.LinksGather;
import org.scraper.model.web.DataBase;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainModel {
	
	private Pool globalPool;
	
	private Pool sitesPool;
	
	private QueuesManager queuesManager;
	
	
	private DataBase dataBase;
	
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
	
	
	public static void main(String[] args) {
		MainModel m = new MainModel(100, 5000, 0, false, false);
	}
	
	public MainModel(int threads, int timeout, int limit, Boolean check, boolean click) {
		//OpenCV lib
		String sysArch = System.getProperty("os.arch");
		sysArch = sysArch.substring(sysArch.length() - 2);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME + "x" + sysArch);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("SHUTDOWN HOOK!!!");
			dataBase.postAll();
			queuesManager.shutdownAll();
		}));
		
		
		setCheckOnFly(check);
		
		globalPool = new Pool(threads);
		sitesPool = new Pool(10);
		
		dataBase = new DataBase(globalPool);
		dataBase.getAll();
		
		
		queuesManager = new QueuesManager(globalPool, threads / 10, threads / 10);
		
		proxyManager = new ProxyManager(limit);
		
		sitesManager = new SitesManager(dataBase);
		
		ip = ConnectionChecker.getIp();
		
		scrapersFactory = new ScrapersFactory(globalPool, queuesManager.getBrowsersQueue(), queuesManager.getOcrQueue());
		
		
		checker = new ProxyChecker(globalPool, timeout, getProxyManager().getAll());
		
		assigner = new AssignManager(scrapersFactory, checker, globalPool, checkOnFly, dataBase.getAllDomains());
		
		scraper = new ProxyScraper(scrapersFactory, globalPool, dataBase.getAllDomains());
		scraper.setAssigner(assigner);
		
		
		gather = new LinksGather(2, globalPool);
		
		observer = new GlobalObserver(proxyManager, sitesManager, assigner, checker, checkOnFly);
		checker.addObserver(observer);
		scraper.addObserver(observer);
		assigner.addObserver(observer);
		gather.addObserver(observer);
		
		MainModel.log.info("Initialized");
		
		synchronized (globalPool) {
			while (!(globalPool.getPool().getActiveCount() == 0))
				globalPool.notify();
		}
	}
	
	public MainModel() {
		this(100, 3000, 0, false, false);
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
	
	public Pool pool() {
		return globalPool;
	}
	
	public Pool getSitesPool() {
		return sitesPool;
	}
	
	public DataBase getDataBase() {
		return dataBase;
	}
	
	public ScrapersFactory getScrapersFactory() {
		return scrapersFactory;
	}
	
	public QueuesManager getQueuesManager() {
		return queuesManager;
	}
	
	public ProxyChecker getChecker() {
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
		Interval.addFunc("threads", () -> globalPool.getThreads() - globalPool.getPool().getActiveCount());
		Interval.addFunc("threadsMax", () -> globalPool.getThreads());
		
		Interval.addFunc("browsers", () -> queuesManager.getBrowsersQueue().size());
		Interval.addFunc("browsersMax", () -> queuesManager.getBrowsersNumber());
		
		Interval.addFunc("ocrs", () -> queuesManager.getOcrQueue().size());
		Interval.addFunc("ocrsMax", () -> queuesManager.getOcrNumber());
	}
	
	public java.util.concurrent.atomic.AtomicBoolean getCheckOnFly() {
		return checkOnFly;
	}
}
