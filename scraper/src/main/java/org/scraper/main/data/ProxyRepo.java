package org.scraper.main.data;

import org.scraper.MVC.model.ProxyUtility;
import org.scraper.main.Pool;
import org.scraper.main.Proxy;
import org.scraper.main.limiter.Limiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

public class ProxyRepo extends Observable {
	
	private List<Proxy> all = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> checked = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> working = Collections.synchronizedList(new ArrayList<>());
	
	private ProxyUtility model;
	
	private Limiter limiter;
	
	public ProxyRepo(Limiter limiter) {
		this.limiter = limiter;
	}
	
	public void addProxy(Proxy proxy) {
		if (!all.contains(proxy)) {
			all.add(proxy);
			if (proxy.isWorking()) {
				working.add(proxy);
				limiter.incrementBy(1);
			}
			if (proxy.isChecked()) checked.add(proxy);
			if (model != null) model.addProxy(proxy);
			setChanged();
			notifyObservers(proxy);
		}
		/*if (limit != 0 && working.size() >= limit && Pool.getInstance().isEnabled()){
			Pool.getInstance().pause();
		}*/
	}
	
	public Proxy getIfPresent(Proxy proxy) {
		return (all.contains(proxy)) ? all.get(all.indexOf(proxy)) : proxy;
	}
	
	public void addProxies(List<Proxy> all) {
		all.forEach(this::addProxy);
	}
	
	public void setModel(ProxyUtility model) {
		this.model = model;
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
	
	public void clear() {
		all.clear();
		checked.clear();
		working.clear();
	}
}
