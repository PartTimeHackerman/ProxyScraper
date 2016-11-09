package org.scraper.model.managers;

import org.scraper.model.IConcurrent;
import org.scraper.model.IPool;
import org.scraper.model.Proxy;
import org.scraper.model.checker.IProxyChecker;
import org.scraper.model.managers.AssignManager;
import org.scraper.model.scrapers.ScrapersFactory;
import org.scraper.model.web.Domain;
import org.scraper.model.web.Site;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AssignManagerConcurrent extends AssignManager implements IConcurrent{
	
	public AssignManagerConcurrent(ScrapersFactory scrapersFactory, IProxyChecker checker, AtomicBoolean checkOnFly, List<Domain> domains) {
		super(scrapersFactory, checker, checkOnFly, domains);
	}
	
	@Override
	public List<Proxy>assign(Site site) {
		return send(() -> super.assign(site));
	}
	
}
