package org.scraper.model;


import org.opencv.core.Core;
import org.scraper.model.assigner.AssignManager;
import org.scraper.model.checker.ConnectionChecker;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.managers.ProxyManager;
import org.scraper.model.managers.QueuesManager;
import org.scraper.model.scrapers.ProxyScraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.*;

public class Globals {
	
	private Pool globalPool;
	
	private Pool sitesPool;
	
	private QueuesManager queues;
	
	
	private DataBase dataBase;
	
	private ProxyManager proxyManager;
	
	
	private GlobalObserver observer;
	
	private LinksManager linksManager;
	
	private ScrapersFactory scrapersFactory;
	
	private ProxyChecker checker;
	
	private ProxyScraper scraper;
	
	private AssignManager assigner;
	
	
	private Boolean checkOnFly;
	
	private String ip;
	
	
	public Globals(int threads, int timeout, int limit, boolean checkOnFly, boolean click) {
		//OpenCV lib
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		this.checkOnFly = checkOnFly;
		
		globalPool = new Pool(threads);
		sitesPool = new Pool(10);
		
		queues = new QueuesManager(globalPool, threads / 10, threads / 10);
		
		setCheckOnFly(checkOnFly);
		
		dataBase = new DataBase(globalPool);
		dataBase.getAll();
		
		linksManager = new LinksManager(globalPool, queues.getBrowsersQueue(), dataBase);
		linksManager.setOn(click);
		
		observer = null;
		
		ip = ConnectionChecker.getIp();
		
		scrapersFactory = new ScrapersFactory(globalPool, queues.getBrowsersQueue(), queues.getOcrQueue());
		
		checker = new ProxyChecker(globalPool, timeout);
		
		scraper = new ProxyScraper(scrapersFactory);
		
		assigner = new AssignManager(scrapersFactory, checker, checkOnFly);
		
		proxyManager = new ProxyManager(limit);
		
		synchronized (globalPool) {
			while (!(globalPool.getPool().getActiveCount() == 0))
				globalPool.notify();
		}
	}
	
	public Globals() {
		this(100, 3000, 0, false, false);
	}
	
	public boolean isCheckOnFly() {
		return checkOnFly;
	}
	
	public void setCheckOnFly(boolean checkOnFly) {
		this.checkOnFly = checkOnFly;
	}
	
	
	public GlobalObserver getObserver() {
		return observer;
	}
	
	public LinksManager getLinksManager() {
		return linksManager;
	}
	
	public String getIp() {
		return ip;
	}
	
	public Pool getGlobalPool() {
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
	
	public QueuesManager getQueues() {
		return queues;
	}
	
	public ProxyChecker getChecker() {
		return checker;
	}
	
	public ProxyScraper getScraper() {
		return scraper;
	}
	
	public Boolean getCheckOnFly() {
		return checkOnFly;
	}
	
	public ProxyManager getProxyManager() {
		return proxyManager;
	}
	
	public AssignManager getAssigner() {
		return assigner;
	}
	
	public void setCheckOnFly(Boolean checkOnFly) {
		this.checkOnFly = checkOnFly;
	}
}
