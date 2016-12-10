package org.scraper.main.manager;

import org.scraper.main.MainLogger;
import org.scraper.main.MainPool;
import org.scraper.main.Proxy;
import org.scraper.main.assigner.*;
import org.scraper.main.checker.IProxyChecker;
import org.scraper.main.scraper.ScrapeType;
import org.scraper.main.scraper.ScrapersFactory;
import org.scraper.main.web.Domain;
import org.scraper.main.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class AssignManager extends Observable {
	
	private IProxyChecker checker;
	
	private List<Domain> domains;
	
	private AtomicBoolean checkOnFly;
	
	private ScrapersFactory scrapersFactory;
	
	public AssignManager(ScrapersFactory scrapersFactory, IProxyChecker checker, AtomicBoolean checkOnFly, List<Domain> domains) {
		this.checker = checker;
		this.checkOnFly = checkOnFly;
		this.domains = domains;
		this.scrapersFactory = scrapersFactory;
	}
	
	public List<Proxy> assign(Site site) {
		
		List<Proxy> proxy = domains.contains(site.getDomain())
				? assignWithDomain(site)
				: assignWithoutDomain(site);
		
		setChanged();
		notifyObservers(proxy);
		
		MainLogger.log().info("Site ({}) assigned", site);
		
		return proxy;
	}
	
	public List<Proxy> assignWithDomain(Site site) {
		Domain siteDomain = site.getDomain();
		
		ScrapeType type = domains.get(domains.indexOf(siteDomain)).getType();
		
		IAssigner assigner = getAssigner();
		ScrapeType newType = assigner.getType(site);
		site.setType(newType);
		
		if (newType != ScrapeType.BLACK
				&& type == ScrapeType.BLACK) {
			addDomain(site.getDomain(), type);
		}
		return assigner.getProxy();
	}
	
	public List<Proxy> assignWithoutDomain(Site site) {
		IAssigner assigner = getAssigner();
		
		ScrapeType type = assigner.getType(site);
		site.setType(type);
		
		addDomain(site.getDomain(), type);
		
		return assigner.getProxy();
	}
	
	private void addDomain(Domain domain, ScrapeType type) {
		Domain newDomain = new Domain(domain.getDomainString(), type);
		domains.add(newDomain);
	}
	
	private IAssigner getAssigner() {
		return checkOnFly.get()
				? new CheckingAssigner(new ChainFinder(scrapersFactory), checker)
				: new NonCheckAssigner(new ChainFinder(scrapersFactory));
	}
	
	public List<Proxy> assignList(List<Site> sites) {
		List<Proxy> proxy = new ArrayList<>();
		
		sites = sites.stream()
				.filter(site -> site.getType() != ScrapeType.BLACK)
				.collect(Collectors.toList());
		
		MainPool.getInstance().subPool(sites, site -> proxy.addAll(assign(site)), QueuesManager.getInstance().getBrowserQueue().getSize());
		
		MainLogger.log().debug("List assign done");
		
		return proxy;
	}
}
