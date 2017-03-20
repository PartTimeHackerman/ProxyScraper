package org.scraper.main.manager;

import org.scraper.main.IConcurrent;
import org.scraper.main.Pool;
import org.scraper.main.Proxy;
import org.scraper.main.checker.IProxyChecker;
import org.scraper.main.data.DomainsRepo;
import org.scraper.main.scraper.ScrapersFactory;
import org.scraper.main.data.Site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class AssignManagerConcurrent extends AssignManager implements IConcurrent {
	
	private Pool pool;
	
	public AssignManagerConcurrent(ScrapersFactory scrapersFactory, IProxyChecker checker, AtomicBoolean checkOnFly, DomainsRepo domainsRepo, Pool pool) {
		super(scrapersFactory, checker, domainsRepo, checkOnFly);
		this.pool = pool;
	}
	
	@Override
	public List<Proxy> assign(Site site) {
		return send(() -> super.assign(site), pool);
	}
	
	@Override
	public List<Proxy> assignList(Collection<Site> sites) {
		List<Callable<List<Proxy>>> calls = new ArrayList<>();
		sites.forEach(site ->
							  calls.add(() ->
												super.assign(site)));
		
		List<List<Proxy>> proxyList = pool.getNewSubPool().sendTasks(calls);
		List<Proxy> proxy = new ArrayList<>();
		proxyList.forEach(proxy::addAll);
		
		return proxy;
	}
	
}
