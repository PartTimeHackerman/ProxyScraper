package org.scraper.main.manager;

import org.scraper.main.Proxy;
import org.scraper.MVC.model.ProxyModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProxyManager {
	
	private Integer limit;
	
	private List<Proxy> all = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> checked = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> working = Collections.synchronizedList(new ArrayList<>());
	
	private ProxyModel model;
	
	public ProxyManager(int limit) {
		setLimit(limit);
	}
	
	public void addProxy(Proxy proxy) {
		if (!all.contains(proxy) && (
		limit == 0 || working.size() < limit )) {
			all.add(proxy);
			if (proxy.isWorking()) working.add(proxy);
			if (proxy.isChecked()) checked.add(proxy);
			if (model != null) model.addProxy(proxy);
			limit++;
		} else {
			//TODO stop everything
		}
	}
	
	public Proxy getIfPresent(Proxy proxy) {
		return (all.contains(proxy)) ? all.get(all.indexOf(proxy)) : proxy;
	}
	
	public void addProxies(List<Proxy> all) {
		all.forEach(this::addProxy);
	}
	
	public void setLimit(int limit) {
		this.limit = limit > 0 ? limit : 0;
	}
	
	public void setModel(ProxyModel model) {
		this.model = model;
	}
	
	public Integer getLimit() {
		return limit;
	}
	
	public List<Proxy> getAll() {
		return all;
	}
	
	public List<Proxy> getChecked() {
		return checked;
	}
	
	public List<Proxy> getWorking() {
		return working;
	}
}
