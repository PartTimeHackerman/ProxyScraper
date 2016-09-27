package org.scraper.model;


import org.opencv.core.Core;
import org.scraper.model.assigner.AssignManager;
import org.scraper.model.checker.ConnectionChecker;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.scraper.ProxyScraper;
import org.scraper.model.scraper.ScrapersFactory;
import org.scraper.model.web.DataBase;
import org.scraper.model.web.LinksManager;

import java.util.List;

public class MainModel implements InterfaceModel{
	
	private Model scrapeModel;
	
	private Model proxyModel;
	
	
	private Model presentModel = scrapeModel;
	
	private Pool globalPool;
	
	private Pool sitesPool;
	
	private QueuesManager queues;
	
	
	private DataBase dataBase;
	
	private ProxyManager proxyManager;
	
	private SitesManager sitesManager;
	
	private TextManager textManager;
	
	
	private GlobalObserver observer;
	
	private LinksManager linksManager;
	
	private ScrapersFactory scrapersFactory;
	
	private ProxyChecker checker;
	
	private ProxyScraper scraper;
	
	private AssignManager assigner;
	
	
	private Boolean checkOnFly;
	
	private String ip;
	
	public static void main(String[] args) {
		MainModel m = new MainModel(100, 5000, 0, false, false);
	}
	
	public MainModel(int threads, int timeout, int limit, boolean checkOnFly, boolean click) {
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
		
		observer = new GlobalObserver(proxyManager);
		
		ip = ConnectionChecker.getIp();
		
		scrapersFactory = new ScrapersFactory(globalPool, queues.getBrowsersQueue(), queues.getOcrQueue());
		
		checker = new ProxyChecker(globalPool, timeout);
		
		scraper = new ProxyScraper(scrapersFactory);
		
		assigner = new AssignManager(scrapersFactory, checker, globalPool, checkOnFly);
		
		proxyManager = new ProxyManager(limit);
		
		sitesManager = new SitesManager(dataBase);
		
		textManager = new TextManager(proxyManager, sitesManager);
		
		scrapeModel = new ScraperModel(this, scraper, dataBase.getAllSites());
		
		proxyModel = new ProxyModel(this);
		
		synchronized (globalPool) {
			while (!(globalPool.getPool().getActiveCount() == 0))
				globalPool.notify();
		}
	}
	
	@Override
	public void scrape() {
		presentModel.scrape();
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
	public void addToText(List<String> text) {
		textManager.addToText(text);
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
	
	public TextManager getTextManager() {
		return textManager;
	}
	
	public void setTextManager(TextManager textManager) {
		this.textManager = textManager;
	}
}
