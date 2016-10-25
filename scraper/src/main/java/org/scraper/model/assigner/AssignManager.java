package org.scraper.model.assigner;

import org.scraper.model.Main;
import org.scraper.model.Pool;
import org.scraper.model.Proxy;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class AssignManager extends Observable {
	
	private ScrapersFactory scrapersFactory;
	
	private ProxyChecker checker;
	
	private Pool pool;
	
	private Boolean check;
	
	public AssignManager(ScrapersFactory scrapersFactory, ProxyChecker checker, Pool pool, Boolean check) {
		this.scrapersFactory = scrapersFactory;
		this.checker = checker;
		this.pool = pool;
		this.check = check;
	}
	
	public AssignManager(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	public AssignManager(int size, int timeout) {
		this.scrapersFactory = new ScrapersFactory(size);
		this.checker = new ProxyChecker(new Pool(size), timeout);
		if (timeout > 0)
			check = true;
	}
	
	public void assignConcurrent(Site site) {
		pool.sendTask(() -> assign(site), false);
	}
	
	public List<Proxy> assign(Site site) {
		
		Assigner assigner;
		if (check) {
			assigner = new CheckingAssigner(scrapersFactory, checker);
		} else {
			assigner = new NonCheckAssigner(scrapersFactory);
		}
		
		site.setType(assigner.getType(site));
		
		setChanged();
		notifyObservers(assigner.getProxy());
		setChanged();
		notifyObservers(site);
		
		Main.log.fatal("Site {} assigned", site);
		
		return assigner.getProxy();
	}
	
	public List<Proxy> assignList(List<Site> sites) {
		
		List<Callable<List<Proxy>>> calls = new ArrayList<>();
		List<List<Proxy>> proxyList;
		sites.stream()
				.map(site ->
							 calls.add(() -> assign(site)))
				.collect(Collectors.toList());
		
		proxyList = pool.sendTasks(calls);
		
		List<Proxy> proxy = new ArrayList<>();
		
		proxyList.forEach(proxy::addAll);
		
		return proxy;
	}
	
	/*
	private boolean alreadyExists(Site site) {
		ScrapeType type;
		Domain siteDomain = site.getDomain();
		List<Domain> domains = globals.getDataBase().getAllDomains();
		
		if (domains.contains(siteDomain)) {
			type = domains.get(domains.indexOf(siteDomain)).getType();
			site.setType(type);
			return true;
		}
		return false;
	}*/
	
	
}
