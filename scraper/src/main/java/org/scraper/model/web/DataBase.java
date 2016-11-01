package org.scraper.model.web;

import org.apache.commons.collections.ListUtils;
import org.scraper.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBase {
	
	private Pool pool;
	
	private List<Site> sites = new ArrayList<>();
	
	private List<Domain> domains = new ArrayList<>();
	
	private List<String> links = new ArrayList<>();
	
	private List<String> clicks = new ArrayList<>();
	
	
	private List<Site> newSites = Collections.synchronizedList(new ArrayList<>());
	
	private List<Domain> newDomains = Collections.synchronizedList(new ArrayList<>());
	
	private List<String> clicked = Collections.synchronizedList(new ArrayList<>());
	
	private Boolean gotSites = false;
	
	
	public DataBase(Pool pool) {
		this.pool = pool;
	}
	
	public void getAll() {
		getAll(sites, domains, clicks, links);
		//TODO wait for database to dl what's needed
		while (!gotSites){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void getAll(List<Site> sites, List<Domain> domains, List<String> clicks, List<String> links) {
		getSites(sites);
		getDomains(domains);
		getClicks(clicks);
		getLinks(links);
	}
	
	public void postNew() {
		postAll(newSites, newDomains, clicked);
	}
	
	public void postAll() {
		postAll(sites, domains, clicked);
	}
	
	private void postAll(List<Site> sites, List<Domain> domains, List<String> clicks) {
		postSites(sites);
		postDomains(domains);
		postClicks(clicks);
	}
	
	private void getSites(List<Site> sites) {
		try {
			pool.sendTask(() -> {
				PHP.get(sites, PHPMethod.GET_SITES);
				gotSites = true;
			}, false);
		} catch (Exception e) {
			org.scraper.model.Main.log.error("Failed to get sites");
		}
	}
	
	private void getDomains(List<Domain> domains) {
		try {
			pool.sendTask(() -> PHP.get(domains, PHPMethod.GET_DOMAINS), false);
		} catch (Exception e) {
			org.scraper.model.Main.log.error("Failed to get domains");
		}
	}
	
	private void getClicks(List<String> clicks) {
		try {
			pool.sendTask(() -> PHP.get(clicks, PHPMethod.GET_CLICKS), false);
		} catch (Exception e) {
			org.scraper.model.Main.log.error("Failed to get sites 2");
		}
	}
	
	private void getLinks(List<String> links) {
		try {
			pool.sendTask(() -> PHP.get(links, PHPMethod.GET_LINKS), false);
		} catch (Exception e) {
			org.scraper.model.Main.log.error("Failed to get sites 3");
		}
	}
	
	public void postSites(List<Site> sites) {
		try {
			pool.sendTask(() -> PHP.post(sites, PHPMethod.POST_SITES), false);
		} catch (Exception e) {
			org.scraper.model.Main.log.error("Failed to submit sites");
		}
	}
	
	private void postDomains(List<Domain> domains) {
		try {
			pool.sendTask(() -> PHP.post(domains, PHPMethod.POST_DOMAINS), false);
		} catch (Exception e) {
			org.scraper.model.Main.log.error("Failed to submit domains");
		}
	}
	
	private void postClicks(List<String> clicks) {
		try {
			pool.sendTask(() -> PHP.post(clicks, PHPMethod.POST_CLICKS), false);
		} catch (Exception e) {
			org.scraper.model.Main.log.error("Failed to submit sites 2");
		}
	}
	
	public List<Site> getAllSites() {
		return ListUtils.sum(sites, newSites);
	}
	
	public List<String> getLinks() {
		return links;
	}
	
	public List<String> getClicks() {
		return clicks;
	}
	
	public List<String> getClicked() {
		return clicked;
	}
	
	public List<Domain> getAllDomains() {
		return ListUtils.sum(domains, newDomains);
	}
	
	
	public void addSite(Site site) {
		if (!sites.contains(site)) {
			sites.add(site);
			newSites.add(site);
		}
	}
	
	public void addDomain(Domain domain) {
		if (!domains.contains(domain)) {
			domains.add(domain);
			newDomains.add(domain);
		}
	}
}
