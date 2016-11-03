package org.scraper.model.managers;

import org.scraper.model.Pool;
import org.scraper.model.Proxy;
import org.scraper.model.assigner.Assigner;
import org.scraper.model.assigner.CheckingAssigner;
import org.scraper.model.assigner.NonCheckAssigner;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.modles.MainModel;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Domain;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;

public class AssignManager extends Observable {
	
	private ScrapersFactory scrapersFactory;
	
	private ProxyChecker checker;
	
	private Pool pool;
	
	private List<Domain> domains;
	
	private AtomicBoolean checkOnFly;
	
	public AssignManager(ScrapersFactory scrapersFactory, ProxyChecker checker, Pool pool, AtomicBoolean checkOnFly, List<Domain> domains) {
		this.scrapersFactory = scrapersFactory;
		this.checker = checker;
		this.pool = pool;
		this.checkOnFly = checkOnFly;
		this.domains = domains;
	}
	
	public List<Proxy> assignConcurrent(Site site) {
		return pool.sendTask(() -> assign(site), false);
	}
	
	public List<Proxy>assign(Site site) {
		
		List<Proxy> proxy = !domains.contains(site.getDomain())
				? assignWithDomain(site)
				: assignWithoutDomain(site);
		
		setChanged();
		notifyObservers(proxy);
		
		MainModel.log.fatal("Site {} assigned", site);
		
		return proxy;
	}
	
	public List<Proxy> assignWithDomain(Site site) {
		Domain siteDomain = site.getDomain();
		
		ScrapeType type = domains.get(domains.indexOf(siteDomain)).getType();
		site.setType(type);
		
		Assigner assigner = getAssigner();
		ScrapeType newType = assigner.getType(site);
		
		if (newType != ScrapeType.BLACK
				&& type == ScrapeType.BLACK) {
			addDomain(site.getDomain(), type);
		}
		return assigner.getProxy();
	}
	
	public List<Proxy> assignWithoutDomain(Site site) {
		Assigner assigner = getAssigner();
		assigner.getType(site);
		
		ScrapeType type = assigner.getType(site);
		
		addDomain(site.getDomain(), type);
		
		return assigner.getProxy();
	}
	
	private void addDomain(Domain domain, ScrapeType type) {
		Domain newDomain = new Domain(domain.getDomainString(), type);
		domains.add(newDomain);
	}
	
	private Assigner getAssigner() {
		return checkOnFly.get()
				? new CheckingAssigner(scrapersFactory, checker)
				: new NonCheckAssigner(scrapersFactory);
	}
	
	public List<Proxy> assignList(List<Site> sites) {
		List<Proxy> proxy = new ArrayList<>();
		
		sites.parallelStream()
				.filter(site -> site.getType() != ScrapeType.BLACK)
				.forEach(site -> proxy.addAll(assign(site)));
		
		return proxy;
	}
}
