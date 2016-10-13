package org.scraper.model.modles;


import org.opencv.core.Core;
import org.scraper.model.*;
import org.scraper.model.assigner.AssignManager;
import org.scraper.model.checker.ConnectionChecker;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.managers.ProxyManager;
import org.scraper.model.managers.QueuesManager;
import org.scraper.model.managers.SitesManager;
import org.scraper.model.managers.ProxyTableManager;
import org.scraper.model.scrapers.ProxyScraper;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.gather.LinksGather;
import org.scraper.model.web.DataBase;
import org.scraper.model.web.LinksManager;

public class MainModel implements InterfaceModel{
	
	private Model scrapeModel;
	
	private Model proxyModel;
	
	
	private Model presentModel;
	
	private Pool globalPool;
	
	private Pool sitesPool;
	
	private QueuesManager queues;
	
	
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
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		globalPool = new Pool(threads);
		sitesPool = new Pool(10);
		
		queues = new QueuesManager(globalPool, threads / 10, threads / 10);
		
		setCheckOnFly(check);
		
		dataBase = new DataBase(globalPool);
		dataBase.getAll();
		
		linksManager = new LinksManager(globalPool, queues.getBrowsersQueue(), dataBase);
		linksManager.setOn(click);
		
		
		ip = ConnectionChecker.getIp();
		
		scrapersFactory = new ScrapersFactory(globalPool, queues.getBrowsersQueue(), queues.getOcrQueue());
		
		
		checker = new ProxyChecker(globalPool, timeout);
		
		scraper = new ProxyScraper(scrapersFactory, globalPool);
		
		assigner = new AssignManager(scrapersFactory, checker, globalPool, checkOnFly);
		
		gather = new LinksGather(2, globalPool);
		
		
		
		
		proxyManager = new ProxyManager(limit);
		
		sitesManager = new SitesManager(dataBase);
		
		ProxyTableManager.setProxyManager(proxyManager);
		ProxyTableManager.setSitesManager(sitesManager);
		
		scrapeModel = new ScraperModel(this, scraper, dataBase.getAllSites());
		
		proxyModel = new ProxyModel(this);
		
		presentModel = scrapeModel;
		
		observer = new GlobalObserver(proxyManager, sitesManager, assigner, checker, checkOnFly);
		checker.addObserver(observer);
		scraper.addObserver(observer);
		assigner.addObserver(observer);
		gather.addObserver(observer);
		
		
		
		synchronized (globalPool) {
			while (!(globalPool.getPool().getActiveCount() == 0))
				globalPool.notify();
		}
	}
	
	public MainModel(){
	this(100, 3000, 0, true, false);
	}
	
	@Override
	public void scrape() {
		pool().sendTask(() -> presentModel.scrape(), false);
	}
	
	@Override
	public void check() {
		presentModel.check();
	}
	
	@Override
	public void crawl() {
		presentModel.crawl();
	}
	
	@Override
	public void setCheckOnFly(boolean checkOnFly) {
		this.checkOnFly = checkOnFly;
	}
	
	@Override
	public void switchModel() {
		//TODO switch to proxy model if textarea doesn't contains any site
		presentModel = (presentModel == scrapeModel) ? proxyModel : scrapeModel;
	}
	
	@Override
	public void save() {
		
	}
	
	@Override
	public void load() {
		
	}
	
	@Override
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
	
	public QueuesManager getQueues() {
		return queues;
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
	
}
