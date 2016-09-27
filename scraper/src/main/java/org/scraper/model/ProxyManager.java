package org.scraper.model;

import org.scraper.model.web.DataBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProxyManager {
	
	private int limit;
	
	private List<Proxy> all = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> checked = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> working = Collections.synchronizedList(new ArrayList<>());
	
	public ProxyManager(int limit) {
		setLimit(limit);
	}
	
	public void addProxy(Proxy proxy) {
		if (!all.contains(proxy) && working.size() < limit) {
			all.add(proxy);
			if (proxy.isWorking()) working.add(proxy);
			if (proxy.isChecked()) checked.add(proxy);
		} else {
			//TODO stop everything
		}
	}
	
	public Proxy getIfPresent(Proxy proxy){
		return (all.contains(proxy)) ? all.get(all.indexOf(proxy)) : proxy;
	}
	
	public void addProxies(List<Proxy> all) {
		all.forEach(this::addProxy);
	}
	
	public void setLimit(int limit) {
		this.limit = limit > 0 ? Integer.MAX_VALUE : limit;
	}
}
