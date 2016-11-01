package org.scraper.model.modles;


import org.opencv.core.Core;
import org.scraper.model.*;
import org.scraper.model.managers.AssignManager;
import org.scraper.model.checker.ConnectionChecker;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.managers.ProxyManager;
import org.scraper.model.managers.QueuesManager;
import org.scraper.model.managers.SitesManager;
import org.scraper.model.scrapers.ProxyScraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.gather.LinksGather;
import org.scraper.model.web.DataBase;
import org.scraper.model.web.LinksManager;

public class MainModel {
	
	//private SitesModel scrapeModel;
	
	//private ProxyModel proxyModel;
	
	
	private Model presentModel;
	
	private Pool globalPool;
	
	private Pool sitesPool;
	
	private QueuesManager queuesManager;
	
	
	private DataBase dataBase;
	
	private ProxyManager proxyManager;
	
	private SitesManager sitesManager;
	
	
	private GlobalObserver observer;
	
	private LinksManager linksManager;
	
	private ScrapersFactory scrapersFactory;
	
	private ProxyChecker checker;
	
	private ProxyScraper scraper;
	
	private AssignManager assigner;
	
	private LinksGather gather;
	
	
	private Boolean checkOnFly;
	
	private String ip;
	
	
	public static void main(String[] args) {
		MainModel m = new MainModel(100, 5000, 0, false, false);
	}
	
	public MainModel(int threads, int timeout, int limit, boolean check, boolean click) {
		//OpenCV lib
		String sysArch = System.getProperty("os.arch");
		sysArch = sysArch.substring(sysArch.length()-2);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME + "x" + sysArch );
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Main.log.info("SHUTDOWN HOOK!!!");
			dataBase.postAll();
			queuesManager.shutdownBrowsers();
		}));
		
		
		setCheckOnFly(check);
		
		globalPool = new Pool(threads);
		sitesPool = new Pool(10);
		
		dataBase = new DataBase(globalPool);
		dataBase.getAll();
		
		
		queuesManager = new QueuesManager(globalPool, threads / 10, threads / 10);
		
		linksManager = new LinksManager(globalPool, queuesManager.getBrowsersQueue(), dataBase);
		linksManager.setOn(click);
		
		proxyManager = new ProxyManager(limit);
		
		sitesManager = new SitesManager(dataBase);
		
		ip = ConnectionChecker.getIp();
		
		scrapersFactory = new ScrapersFactory(globalPool, queuesManager.getBrowsersQueue(), queuesManager.getOcrQueue());
		
		
		checker = new ProxyChecker(globalPool, timeout, getProxyManager().getAll());
		
		assigner = new AssignManager(scrapersFactory, checker, globalPool, this);
		
		scraper = new ProxyScraper(scrapersFactory, globalPool, dataBase.getAllDomains());
		scraper.setAssigner(assigner);
		
		
		gather = new LinksGather(2, globalPool);
		
		
		//scrapeModel = new SitesModel(assigner, scraper, gather);
		
		//proxyModel = new ProxyModel(checker);
		
		observer = new GlobalObserver(proxyManager, sitesManager, assigner, checker, this);
		checker.addObserver(observer);
		scraper.addObserver(observer);
		assigner.addObserver(observer);
		gather.addObserver(observer);
		
		Main.log.info("Initialized");
		
		synchronized (globalPool) {
			while (!(globalPool.getPool().getActiveCount() == 0))
				globalPool.notify();
		}
	}
	
	public MainModel(){
	this(100, 3000, 0, false, false);
	}
	
	//@Override
	public void scrape() {
		pool().sendTask(() -> presentModel.scrape(), false);
	}
	
	//@Override
	public void check() {
		presentModel.check();
	}
	
	//@Override
	public void crawl() {
		presentModel.crawl();
	}
	
	//@Override
	public void setCheckOnFly(boolean checkOnFly) {
		this.checkOnFly = checkOnFly;
	}
	
	//@Override
	public void save() {
		
	}
	
	//@Override
	public void load() {
		
	}
	
	//@Override
	public void setGatherDepth(Integer depth){
		gather.setDepth(depth);
	}
	
	public Model getPresentModel() {
		return presentModel;
	}
	
	public void setPresentModel(Model presentModel) {
		this.presentModel = presentModel;
	}
	
	public boolean isCheckOnFly() {
		return checkOnFly;
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
	
	public boolean getCheckOnFly() {
		return checkOnFly;
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
	
	public void setVarsInterval(){
		Interval.addFunc("threads", () -> globalPool.getThreads() - globalPool.getPool().getActiveCount());
		Interval.addFunc("threadsMax", () -> globalPool.getThreads());
		
		Interval.addFunc("browsers", () -> queuesManager.getBrowsersQueue().size());
		Interval.addFunc("browsersMax", () -> queuesManager.getBrowsersNumber());
		
		Interval.addFunc("ocrs", () -> queuesManager.getOcrQueue().size());
		Interval.addFunc("ocrsMax", () -> queuesManager.getOcrNumber());
	}
	
}
