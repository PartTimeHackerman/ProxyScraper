package scraper;

import scraper.data.Site;
import scraper.filters.SitesFilter;
import scraper.gather.LinksGather;
import scraper.manager.AssignManager;
import scraper.scraper.ProxyScraper;
import scraper.scraper.ScrapeType;
import scraper.web.LanguageCheck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SitesUtility implements IConcurrent {
	
	private static String sitePattern = ".*\\.[a-z]{2,3}.*";
	protected List<Site> all = new ArrayList<>();
	private ProxyScraper scraper;
	private AssignManager assigner;
	private LinksGather gather;
	private SitesFilter sitesFilter = new SitesFilter();
	private Pool pool;
	
	public SitesUtility(AssignManager assigner, ProxyScraper scraper, LinksGather gather, Pool pool) {
		this.assigner = assigner;
		this.scraper = scraper;
		this.gather = gather;
		this.pool = pool;
	}
	
	public void addSite(Site site) {
		if (all.contains(site) || LanguageCheck.isFromOtherLang(site, all))
			return;
		all.add(site);
	}
	
	public void addSite(String siteString) {
		if (!siteString.matches(sitePattern)) return;
		Site site = new Site(siteString, ScrapeType.UNCHECKED);
		addSite(site);
	}
	
	public void scrape(Site site) {
		pool.sendTask(() ->
							  scraper.scrape(site), false);
	}
	
	public void scrape(Collection<Site> sites) {
		pool.sendTask(() ->
							  scraper.scrapeList(sites), false);
	}
	
	public void assgn(Collection<Site> sites) {
		pool.sendTask(() ->
							  assigner.assignList(sites), false);
	}
	
	public void gather(Collection<Site> sites, Integer depth) {
		pool.sendTask(() ->
					  {
						  gather.setDepth(depth);
						  gather.gatherSites(sites);
					  }, false);
	}
	
	public Collection<Site> filterAvgProxies(Integer minSites) {
		return sitesFilter.filterAvgProxies(minSites, all);
	}
	
	public Collection<Site> filterAvgWorking(Integer minWorking) {
		return sitesFilter.filterAvgWorking(minWorking, all);
	}
	
	public SitesFilter getSitesFilter() {
		return sitesFilter;
	}
}
