package scraper.manager;

import scraper.MainLogger;
import scraper.Proxy;
import scraper.assigner.ChainFinder;
import scraper.assigner.CheckingAssigner;
import scraper.assigner.IAssigner;
import scraper.assigner.NonCheckAssigner;
import scraper.checker.IProxyChecker;
import scraper.data.Domain;
import scraper.data.DomainsRepo;
import scraper.data.Site;
import scraper.scraper.ScrapeType;
import scraper.scraper.ScrapersFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class AssignManager extends Observable {
	
	private IProxyChecker checker;
	
	private DomainsRepo domainsRepo;
	
	private AtomicBoolean checkOnFly;
	
	private ScrapersFactory scrapersFactory;
	
	public AssignManager(ScrapersFactory scrapersFactory, IProxyChecker checker, DomainsRepo domainsRepo, AtomicBoolean checkOnFly) {
		this.checker = checker;
		this.domainsRepo = domainsRepo;
		this.checkOnFly = checkOnFly;
		this.scrapersFactory = scrapersFactory;
	}
	
	public List<Proxy> assign(Site site) {
		
		List<Proxy> proxy = domainsRepo.contains(site.getDomain())
				? assignWithDomain(site)
				: assignWithoutDomain(site);
		
		setChanged();
		notifyObservers(proxy);
		
		MainLogger.log(this).info("Site ({}) assigned", site);
		
		return proxy;
	}
	
	public List<Proxy> assignWithDomain(Site site) {
		Domain siteDomain = site.getDomain();
		
		ScrapeType type = domainsRepo.getDomainScrapeType(siteDomain);
		
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
		domainsRepo.add(newDomain);
	}
	
	private IAssigner getAssigner() {
		return checkOnFly.get()
				? new CheckingAssigner(new ChainFinder(scrapersFactory), checker)
				: new NonCheckAssigner(new ChainFinder(scrapersFactory));
	}
	
	public List<Proxy> assignList(Collection<Site> sites) {
		List<Proxy> proxy = new ArrayList<>();
		
		sites = sites.stream()
				.filter(site -> site.getType() != ScrapeType.BLACK)
				.collect(Collectors.toList());
		
		sites.forEach(site ->
							  proxy.addAll(assign(site)));
		
		MainLogger.log(this).debug("List assign done");
		
		return proxy;
	}
}
