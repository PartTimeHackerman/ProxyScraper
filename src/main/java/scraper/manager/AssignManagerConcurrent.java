package scraper.manager;

import scraper.IConcurrent;
import scraper.Pool;
import scraper.Proxy;
import scraper.checker.IProxyChecker;
import scraper.data.DomainsRepo;
import scraper.scraper.ScrapersFactory;
import scraper.data.Site;

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
