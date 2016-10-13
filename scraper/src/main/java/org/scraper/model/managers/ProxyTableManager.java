package org.scraper.model.managers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scraper.model.Proxy;
import org.scraper.model.scrapers.RegexMatcher;

public class ProxyTableManager {
	
	private static ObservableList<Proxy> visible = FXCollections.observableArrayList();
	private static ObservableList<Proxy> all = FXCollections.observableArrayList();
	
	private static Boolean showBroken = false;
	private static Proxy.Type visibleType = Proxy.Type.ALL;
	private static Proxy.Anonymity visibleAnonymity = Proxy.Anonymity.ALL;
	
	private static ProxyManager proxyManager;
	private static SitesManager sitesManager;
	
	private static String proxyPattern = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}.*[0-9]";
	
	public static void addProxy(Proxy proxy) {
		Platform.runLater(() -> {
			all.add(proxy);
			if (proxy.isWorking() || showBroken)
				visible.add(proxy);
		});
	}
	
	public static void addProxy(String proxyString) {
		if (proxyString.matches(proxyPattern)) {
			Proxy proxy = RegexMatcher.matchOne(proxyString);
			proxy = proxyManager.getIfPresent(proxy);
			visible.add(proxy);
		}
		/*
		if (proxyOrSite.matches(sitePattern)) {
			proxyOrSite = (proxyOrSite.contains("http")) ? proxyOrSite : "http://" + proxyOrSite;
			Site site = new Site(proxyOrSite, ScrapeType.UNCHECKED);
			site = sitesManager.getIfPresent(site);
			visible.add(site);
		}*/
	}
	
	public static void filterType(Proxy.Type type) {
		visible.clear();
		visibleType = type;
			all.forEach(proxy -> {
				if (proxy.getType() == type && proxy.getAnonymity() == visibleAnonymity)
					visible.add(proxy);
			});
	}
	
	public static void filterAnonymity(Proxy.Anonymity anonymity) {
		visible.clear();
		visibleAnonymity = anonymity;
		
			all.forEach(proxy -> {
				if (proxy.getAnonymity() == anonymity && proxy.getType() == visibleType)
					visible.add(proxy);
			});
	}
	
	public static void filterTimeout(Float time){
		visible.clear();
		all.forEach(proxy ->{
			if(proxy.getSpeed() <= time)
				visible.add(proxy);
		});
	}
	
	public static void showBroken(boolean show) {
		showBroken = show;
		visible.clear();
		
		all.forEach(proxy -> {
			if (proxy.isWorking() || show)
				visible.add(proxy);
		});
	}
	
	public static void setSitesManager(SitesManager sitesManager) {
		ProxyTableManager.sitesManager = sitesManager;
	}
	
	public static void setProxyManager(ProxyManager proxyManager) {
		ProxyTableManager.proxyManager = proxyManager;
	}
	
	public static ObservableList<Proxy> getVisible() {
		return visible;
	}
	
}
