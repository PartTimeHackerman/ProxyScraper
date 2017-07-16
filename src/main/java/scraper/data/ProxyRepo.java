package scraper.data;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import scraper.Proxy;
import scraper.ProxyUtility;
import scraper.limiter.Limiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProxyRepo {
	
	private List<Proxy> all = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> checked = Collections.synchronizedList(new ArrayList<>());
	
	private List<Proxy> working = Collections.synchronizedList(new ArrayList<>());
	
	private Subject<Proxy, Proxy> workingStream = PublishSubject.create();
	
	private ProxyUtility model;
	
	private Limiter limiter;
	
	public ProxyRepo(Limiter limiter) {
		this.limiter = limiter;
	}
	
	public void addProxy(Proxy proxy) {
		
		if (!all.contains(proxy))
			all.add(proxy);
		
		if (proxy.isWorking()) {
			working.add(proxy);
			limiter.incrementBy(1);
			workingStream.onNext(proxy);
		}
		
		if (proxy.isChecked())
			checked.add(proxy);
		
		if (model != null)
			model.addProxy(proxy);
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
	
	public Observable<Proxy> getWorkingStream() {
		return workingStream;
	}
}
