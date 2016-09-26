package org.scraper.model;


import org.opencv.core.Core;
import org.scraper.model.checker.ConnectionChecker;
import org.scraper.model.scrapers.OCR;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.*;

import java.util.*;
import java.util.concurrent.*;

public class Globals {
	
	private int threads;
	
	private int timeout;
	
	private int limit;
	
	private boolean checkOnFly;
	
	private String ip;
	
	
	private Pool globalPool;
	
	private Pool sitesPool;
	
	
	private int browsersNumber;
	
	private List<Browser> browsers = Collections.synchronizedList(new ArrayList<>());
	
	private BlockingQueue<Browser> browsersQueue;
	
	
	private int ocrNumber;
	
	private List<OCR> ocrs = Collections.synchronizedList(new ArrayList<>());
	
	private BlockingQueue<OCR> ocrQueue;
	
	
	private List<Proxy> allPrx = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> checkedPrx = Collections.synchronizedList(new ArrayList<>());
	
	
	private DataBase dataBase;
	
	private GlobalObserver observer;
	
	private LinksManager linksManager;
	
	private ScrapersFactory scrapersFactory;
	
	public Globals(int threads, int timeout, int limit, boolean checkOnFly, boolean click) {
		//OpenCV lib
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		globalPool = new Pool(threads);
		sitesPool = new Pool(10);
		
		setThreads(threads);
		setTimeout(timeout);
		setLimit( limit>0 ? Integer.MAX_VALUE : limit);
		setCheckOnFly(checkOnFly);
		setBrowsersNumber(threads / 10);
		setOcrNumber(threads / 10);
		
		dataBase = new DataBase(globalPool);
		
		linksManager = new LinksManager(globalPool, browsersQueue, dataBase);
		linksManager.setOn(click);
		
		observer = new GlobalObserver(this);
		
		ip = ConnectionChecker.getIp();
		
		browsersQueue = new ArrayBlockingQueue<>(1024);
		
		ocrQueue = new ArrayBlockingQueue<>(ocrNumber);
		
		scrapersFactory = new ScrapersFactory(globalPool, browsersQueue, ocrQueue);
		
		Callable<?> makeBrowsers = () -> {
			Browser browser = new Browser();
			browsersQueue.put(browser);
			browsers.add(browser);
			return null;
		};
		
		Callable<?> makeOCR = () -> {
			OCR ocr = new OCR();
			ocrQueue.put(ocr);
			ocrs.add(ocr);
			return null;
		};
		
		try {
			globalPool.sendTask(makeBrowsers, false, browsersNumber);
			
		} catch (Exception e) {
			Main.log.error("Failed to load browsers");
		}
		
		try {
			globalPool.sendTask(makeOCR, false, ocrNumber);
		} catch (Exception e) {
			Main.log.error("Failed to load OCR");
		}
		
		synchronized (globalPool) {
			while (! (globalPool.getPool().getActiveCount() == 0))
				globalPool.notify();
		}
		
		Main.log.info("Browser created");
		Main.log.info("OCR created");
	}
	
	public Globals(){
		this(100,3000,0,false,false);
	}
	
	public void addProxy(Proxy proxy) {
		if (! proxy.isChecked() && ! allPrx.contains(proxy)) {
			allPrx.add(proxy);
		} else {
			allPrx.add(proxy);
			if (! checkedPrx.contains(proxy)) checkedPrx.add(proxy);
		}
	}
	
	public void addProxies(List<Proxy> all) {
		all.forEach(this::addProxy);
	}
	
	public int getThreads() {
		return threads;
	}
	
	public void setThreads(int threads) {
		this.threads = threads;
		globalPool.getPool().setMaximumPoolSize(threads);
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public boolean isCheckOnFly() {
		return checkOnFly;
	}
	
	public void setCheckOnFly(boolean checkOnFly) {
		this.checkOnFly = checkOnFly;
	}
	
	public List<Browser> getBrowsers() {
		return browsers;
	}
	
	public List<Proxy> getCheckedPrx() {
		return checkedPrx;
	}
	
	public int getBrowsersNumber() {
		return browsersNumber;
	}
	
	public void setBrowsersNumber(int browsersNumber) {
		this.browsersNumber = browsersNumber <= 0 ? 1 : browsersNumber;
	}
	
	public BlockingQueue<Browser> getBrowsersQueue() {
		return browsersQueue;
	}
	
	public int getOcrNumber() {
		return ocrNumber;
	}
	
	public void setOcrNumber(int ocrNumber) {
		this.ocrNumber = ocrNumber <= 0 ? 1 : ocrNumber;
	}
	
	public List<OCR> getOcrs() {
		return ocrs;
	}
	
	public BlockingQueue<OCR> getOcrQueue() {
		return ocrQueue;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public GlobalObserver getObserver() {
		return observer;
	}
	
	public List<Proxy> getAllPrx() {
		return allPrx;
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
}
