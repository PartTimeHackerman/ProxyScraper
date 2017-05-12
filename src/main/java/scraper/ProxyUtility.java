package scraper;

import scraper.IConcurrent;
import scraper.MainLogger;
import scraper.Pool;
import scraper.Proxy;
import scraper.checker.IProxyChecker;
import scraper.filters.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProxyUtility implements IConcurrent {
	
	private IProxyChecker checker;
	
	private List<Proxy> all = new ArrayList<>();
	
	protected String proxyPattern = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}.*[0-9]";
	
	protected Boolean showBroken = false;
	
	private ProxiesFilter proxiesFilter = new ProxiesFilter();
	private Pool pool;
	
	public ProxyUtility(IProxyChecker checker, Pool pool) {
		this.checker = checker;
		this.pool = pool;
	}
	
	public void addProxy(Proxy proxy) {
		if (!all.contains(proxy))
			all.add(proxy);
	}
	
	public void addProxy(String proxyString) {
		if (!proxyString.matches(proxyPattern)) {
			MainLogger.log(this).warn("{} isn't a proxy", proxyString);
			return;
		}
		Proxy proxy = new Proxy(proxyString);
		addProxy(proxy);
	}
	
	public void check(List<Proxy> proxies) {
		pool.sendTask(() ->
							  checker.checkProxies(proxies), false);
	}
	
	public Collection<Proxy> filterByType(Proxy.Type type) {
		return proxiesFilter.filterByType(type, all);
	}
	
	public Collection<Proxy> filterByAnonymity(Proxy.Anonymity anonymity) {
		return proxiesFilter.filterByAnonymity(anonymity, all);
	}
	
	public Collection<Proxy> filterByTimeout(Float time) {
		return proxiesFilter.filterByTimeout(time, all);
	}
	
	public Collection<Proxy> filterBroken(boolean broken) {
		showBroken = broken;
		return proxiesFilter.filterBroken(broken, all);
	}
	
	public ProxiesFilter getProxiesFilter() {
		return proxiesFilter;
	}
}
