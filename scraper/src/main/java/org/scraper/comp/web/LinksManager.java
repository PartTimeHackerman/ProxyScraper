package org.scraper.comp.web;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.scraper.comp.*;
import org.scraper.comp.Proxy;
import org.scraper.comp.Main;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class LinksManager {
	
	private Pool pool;
	
	private BlockingQueue<Browser> browsers;
	
	private DataBase dataBase;
	
	private List<String> clicked;
	
	private List<String> links;
	
	private boolean on;
	
	public LinksManager(Pool pool, BlockingQueue<Browser> browsers, DataBase dataBase) {
		this.pool = pool;
		this.browsers = browsers;
		this.dataBase = dataBase;
		clicked = dataBase.getClicked();
		links = dataBase.getLinks();
		
	}
	
	public void clickAll(Proxy proxy) {
		if (on
				&& !dataBase.getClicks().contains(proxy.getIpPort())
				&& !dataBase.getClicked().contains(proxy.getIpPort())
				&& proxy.getAnonymity() == Proxy.Anonymity.ELITE) {
			links.forEach(link -> {
				try {
					pool.sendTask(() -> {
						try {
							Browser browser = browsers.poll();
							if (browser == null) browser = new Browser();
							
							click(link, proxy, browser);
							int handles = browser.getBrowser().getWindowHandles().size();
							
							browsers.put(browser);
							
							clicked.add(proxy.getIpPort());
							Main.log.info("Clicked {} through {}, windows {}, browsers size {}", link, proxy, handles, browsers.size());
						} catch (InterruptedException e) {
							Main.log.warn("Failed to click {} through {}", link, proxy);
						}
					}, false);
				} catch (Exception e) {
					Main.log.warn("Failed to click {} through {}", link, proxy);
				}
			});
		}
	}
	
	public void click(String link, Proxy proxy, Browser browser) {
		browser.changeProxy(proxy);
		long time = System.currentTimeMillis();
		browser.getBrowser().get(link);
		time = System.currentTimeMillis() - time;
		if (time < 5500) {
			try {
				Thread.sleep(5500 - time);
			} catch (InterruptedException e) {
				Main.log.debug("Click interrupted");
			}
		}
		Main.log.debug(time + " " + link);
		((TakesScreenshot) browser.getBrowser()).getScreenshotAs(OutputType.FILE);
		
		browser.changeProxy(null);
	}
	
	public boolean isOn() {
		return on;
	}
	
	public void setOn(boolean on) {
		this.on = on;
	}
}
