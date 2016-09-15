package org.scraper.comp;


import org.scraper.comp.web.Address;
import org.scraper.comp.scrapers.OCR;
import org.scraper.comp.web.PHPMethod;
import org.scraper.comp.web.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Globals {

	private static int threads;

	private static int timeout;

	private static boolean checkOnFly;


	private static ThreadPoolExecutor pool;


	private static int browsersNumber;

	private static List<Browser> browsers = Collections.synchronizedList(new ArrayList<>());

	private static BlockingQueue<Browser> browsersQueue;


	private static int ocrNumber;

	private static List<OCR> ocrs = Collections.synchronizedList(new ArrayList<>());

	private static BlockingQueue<OCR> ocrQueue;


	private static List<Address> sites = new ArrayList<>();

	private static List<String> links = new ArrayList<>();

	private static List<String> clicks = new ArrayList<>();


	private static ConcurrentHashMap<String, String[]> proxies = new ConcurrentHashMap<>();


	public static void init(int threads, int timeout, boolean checkOnFly) throws Exception {
		Pool.init(threads);
		pool = Pool.getPool();

		setThreads(threads);
		setTimeout(timeout);
		setCheckOnFly(checkOnFly);

		setBrowsersNumber(threads/10);
		setOcrNumber(threads/10);



		/*IntStream.range(0, threads).forEach(e -> {
			try {
				Pool.sendTask(
						() -> {
							Main.log.debug("test: " + Thread.currentThread().getName());
							return null;
						}, 1, true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});*/

		Pool.sendTask(() -> PHP.get(sites, PHPMethod.GET_ADDRS),false);

		Pool.sendTask(() -> PHP.get(links, PHPMethod.GET_LINKS),false);

		Pool.sendTask(() -> PHP.get(clicks, PHPMethod.GET_CLICKS),false);

		//clicks.addAll((List<String>)PHP.get(PHPMethod.GET_CLICKS));

		browsersQueue = new ArrayBlockingQueue<>(browsersNumber);

		ocrQueue = new ArrayBlockingQueue<>(ocrNumber);

		Callable<?> makeBrowsers = () -> {
			Browser browser = new Browser();
			browsersQueue.put(browser);
			browsers.add(browser);
			Main.log.debug("Browser created");
			return null;
		};

		Callable<?> makeOCR = () -> {
			OCR ocr = new OCR();
			ocrQueue.put(ocr);
			ocrs.add(ocr);
			Main.log.debug("OCR created");
			return null;
		};

		try {
			//Pool.sendTask(()->{
				Pool.sendTask(makeBrowsers,false,browsersNumber);
				Pool.sendTask(makeOCR,false,ocrNumber);
			//	return null;
			//},1,true);

		} catch (Exception e) {
			e.printStackTrace();
		}

		synchronized(pool) {
			while (!(pool.getActiveCount() == 0))
				pool.notify(); //wait for the queue to become empty
		}


	}

	public static void handleChecked(String[] checked){
		proxies.putIfAbsent(checked[0], Arrays.copyOfRange(checked,1,4));
	}
	
	public static int getThreads() {
		return threads;
	}

	public static void setThreads(int threads) {
		Globals.threads = threads;
		pool.setMaximumPoolSize(threads);
	}

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(int timeout) {
		Globals.timeout = timeout;
	}

	public static boolean isCheckOnFly() {
		return checkOnFly;
	}

	public static void setCheckOnFly(boolean checkOnFly) {
		Globals.checkOnFly = checkOnFly;
	}

	public static ThreadPoolExecutor getPool() {
		return pool;
	}

	public static void setPool(ThreadPoolExecutor pool) {
		Globals.pool = pool;
	}

	public static List<Browser> getBrowsers() {
		return browsers;
	}

	public static void setBrowsers(List<Browser> browsers) {
		Globals.browsers = browsers;
	}

	public static List<String> getLinks() {
		return links;
	}

	public static void setLinks(List<String> links) {
		Globals.links = links;
	}

	public static ConcurrentHashMap<String, String[]> getProxies() {
		return proxies;
	}

	public static void setProxies(ConcurrentHashMap<String, String[]> proxies) {
		Globals.proxies = proxies;
	}

	public static int getBrowsersNumber() {
		return browsersNumber;
	}

	public static void setBrowsersNumber(int browsersNumber) {
		Globals.browsersNumber = browsersNumber;
	}

	public static List<Address> getSites() {
		return sites;
	}

	public static void setSites(List<Address> sites) {
		Globals.sites = sites;
	}

	public static List<String> getClicks() {
		return clicks;
	}

	public static void setClicks(List<String> clicks) {
		Globals.clicks = clicks;
	}

	public static BlockingQueue<Browser> getBrowsersQueue() {
		return browsersQueue;
	}

	public static void setBrowsersQueue(BlockingQueue browsersQueue) {
		Globals.browsersQueue = browsersQueue;
	}

	public static int getOcrNumber() {
		return ocrNumber;
	}

	public static void setOcrNumber(int ocrNumber) {
		Globals.ocrNumber = ocrNumber;
	}

	public static List<OCR> getOcrs() {
		return ocrs;
	}

	public static void setOcrs(List<OCR> ocrs) {
		Globals.ocrs = ocrs;
	}

	public static BlockingQueue<OCR> getOcrQueue() {
		return ocrQueue;
	}

	public static void setOcrQueue(BlockingQueue<OCR> ocrQueue) {
		Globals.ocrQueue = ocrQueue;
	}
}
