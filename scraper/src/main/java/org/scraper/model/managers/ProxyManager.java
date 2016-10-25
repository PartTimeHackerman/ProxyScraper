package org.scraper.model.managers;

import org.scraper.model.Proxy;
import org.scraper.model.modles.ProxyModel;
import org.scraper.model.scrapers.RegexMatcher;
import org.scraper.model.web.DataBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProxyManager {
	
	private int limit;
	
	private List<Proxy> all = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> checked = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> working = Collections.synchronizedList(new ArrayList<>());
	
	private String proxyPattern = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}.*[0-9]";
	
	private ProxyModel model;
	
	public ProxyManager(int limit) {
		setLimit(limit);
	}
	
	public void addProxy(Proxy proxy) {
		if (!all.contains(proxy) && working.size() < limit) {
			all.add(proxy);
			if (proxy.isWorking()) working.add(proxy);
			if (proxy.isChecked()) checked.add(proxy);
			if (model != null) model.addProxy(proxy);
		} else {
			//TODO stop everything
		}
	}
	
	public void addProxy(String proxyString) {
		if (!proxyString.matches(proxyPattern)) return;
		
		Proxy proxy = RegexMatcher.matchOne(proxyString);
		proxy = getIfPresent(proxy);
		addProxy(proxy);
	}
	
	public Proxy getIfPresent(Proxy proxy) {
		return (all.contains(proxy)) ? all.get(all.indexOf(proxy)) : proxy;
	}
	
	public void addProxies(List<Proxy> all) {
		all.forEach(this::addProxy);
	}
	
	public void setLimit(int limit) {
		this.limit = limit > 0 ? limit : Integer.MAX_VALUE;
	}
	
	public void setModel(ProxyModel model) {
		this.model = model;
	}
}
