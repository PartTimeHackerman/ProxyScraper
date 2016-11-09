package org.scraper.model.managers;

import org.scraper.model.IPool;
import org.scraper.model.Proxy;
import org.scraper.model.assigner.IAssigner;
import org.scraper.model.assigner.BestOfAllFinder;
import org.scraper.model.assigner.CheckingAssigner;
import org.scraper.model.assigner.NonCheckAssigner;
import org.scraper.model.checker.IProxyChecker;
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
	
	private IProxyChecker checker;
	
	private List<Domain> domains;
	
	private AtomicBoolean checkOnFly;
	
	public AssignManager(ScrapersFactory scrapersFactory, IProxyChecker checker, AtomicBoolean checkOnFly, List<Domain> domains) {
		this.scrapersFactory = scrapersFactory;
		this.checker = checker;
		this.checkOnFly = checkOnFly;
		this.domains = domains;
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
		
		IAssigner assigner = getAssigner();
		ScrapeType newType = assigner.getType(site);
		
		if (newType != ScrapeType.BLACK
				&& type == ScrapeType.BLACK) {
			addDomain(site.getDomain(), type);
		}
		return assigner.getProxy();
	}
	
	public List<Proxy> assignWithoutDomain(Site site) {
		IAssigner assigner = getAssigner();
		assigner.getType(site);
		
		ScrapeType type = assigner.getType(site);
		
		addDomain(site.getDomain(), type);
		
		return assigner.getProxy();
	}
	
	private void addDomain(Domain domain, ScrapeType type) {
		Domain newDomain = new Domain(domain.getDomainString(), type);
		domains.add(newDomain);
	}
	
	private IAssigner getAssigner() {
		return checkOnFly.get()
				? new CheckingAssigner(new BestOfAllFinder(scrapersFactory), checker)
				: new NonCheckAssigner(new BestOfAllFinder(scrapersFactory));
	}
	
	public List<Proxy> assignList(List<Site> sites) {
		List<Proxy> proxy = new ArrayList<>();
		
		sites.parallelStream()
				.filter(site -> site.getType() != ScrapeType.BLACK)
				.forEach(site -> proxy.addAll(assign(site)));
		
		return proxy;
	}
}
