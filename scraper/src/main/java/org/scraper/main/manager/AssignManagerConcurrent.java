package org.scraper.main.manager;

import org.scraper.main.IConcurrent;
import org.scraper.main.Proxy;
import org.scraper.main.checker.IProxyChecker;
import org.scraper.main.scraper.ScrapersFactory;
import org.scraper.main.web.Domain;
import org.scraper.main.web.Site;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AssignManagerConcurrent extends AssignManager implements IConcurrent{
	
	public AssignManagerConcurrent(ScrapersFactory scrapersFactory, IProxyChecker checker, AtomicBoolean checkOnFly, List<Domain> domains) {
		super(scrapersFactory, checker, checkOnFly, domains);
	}
	
	@Override
	public List<Proxy> assign(Site site) {
		return send(() -> super.assign(site));
	}
	
}
