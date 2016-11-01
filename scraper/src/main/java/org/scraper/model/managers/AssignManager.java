package org.scraper.model.managers;

import org.scraper.model.Main;
import org.scraper.model.Pool;
import org.scraper.model.Proxy;
import org.scraper.model.assigner.Assigner;
import org.scraper.model.assigner.CheckingAssigner;
import org.scraper.model.assigner.NonCheckAssigner;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.modles.MainModel;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AssignManager extends Observable {
	
	private ScrapersFactory scrapersFactory;
	
	private ProxyChecker checker;
	
	private Pool pool;
	
	private MainModel model;
	
	private Boolean check;
	
	public AssignManager(ScrapersFactory scrapersFactory, ProxyChecker checker, Pool pool, MainModel model) {
		this.scrapersFactory = scrapersFactory;
		this.checker = checker;
		this.pool = pool;
		this.model = model;
	}
	
	public AssignManager(ScrapersFactory scrapersFactory) {
		this.scrapersFactory = scrapersFactory;
	}
	
	public AssignManager(int size, int timeout) {
		this.scrapersFactory = new ScrapersFactory(size);
		this.checker = new ProxyChecker(new Pool(size), timeout, new ArrayList<>());
		if (timeout > 0)
			check = true;
	}
	
	public List<Proxy> assignConcurrent(Site site) {
		return pool.sendTask(() -> assign(site), false);
	}
	
	public List<Proxy> assign(Site site) {
		List<Proxy> proxy = new ArrayList<>();
		
		if (model.getDataBase().getAllDomains().contains(site.getDomain())) {
			site.setType(model.getDataBase().getAllDomains().get(model.getDataBase().getAllDomains().indexOf(site.getDomain())).getType());
			if (site.getType() != ScrapeType.UNCHECKED && site.getType() != ScrapeType.BLACK && site.getType() != null) {
				try {
					proxy = scrapersFactory.get(site.getType()).scrape(site);
					Assigner.setAvgProxies(site, proxy.size());
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
				if (model.isCheckOnFly()) {
					Assigner.setAvgWorking(site, CheckingAssigner.workingPrecent(proxy, checker));
				}
			}
		} else {
			
			Assigner assigner;
			if (model.isCheckOnFly()) {
				assigner = new CheckingAssigner(scrapersFactory, checker);
			} else {
				assigner = new NonCheckAssigner(scrapersFactory);
			}
			
			ScrapeType type = assigner.getType(site);
			proxy = assigner.getProxy();
			
			site.setType(type);
			
			model.getDataBase().addDomain(site.getDomain());
		}
		
		setChanged();
		notifyObservers(proxy);
		setChanged();
		notifyObservers(site);
		
		Main.log.fatal("Site {} assigned", site);
		
		return proxy;
	}
	
	public List<Proxy> assignList(List<Site> sites) {
		List<Proxy> proxy = new ArrayList<>();
		
		sites.parallelStream().filter(site -> site.getType() != ScrapeType.BLACK).forEach(site -> proxy.addAll(assign(site)));
		return proxy;
		
			
	}
	
	
	/*public List<Proxy> assignList(List<Site> sites) {
		
		BlockingQueue<Site> sitesQueue = new LinkedBlockingDeque<>(5);
		
		pool.sendTask(() -> {
			sites.forEach(site -> {
				try {
					sitesQueue.put(site);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}, false);
		
		
		List<List<Proxy>> proxyList = new ArrayList<>();
		
		List<Proxy> proxy = new ArrayList<>();
		
		Site polled;
		try {
			while ((polled = sitesQueue.poll(500, TimeUnit.MILLISECONDS)) != null) {
				proxyList.add(assignConcurrent(polled));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		*//*List<Callable<List<Proxy>>> calls = new ArrayList<>();
		sites.stream()
				.map(site ->
							 calls.add(() -> assign(sitesQueue.poll(500, TimeUnit.MILLISECONDS))))
				.collect(Collectors.toList());
		
		proxyList = pool.sendTasks(calls);
		*//*
		
		proxyList.forEach(proxy::addAll);
		
		return proxy;
	}*/
	
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
