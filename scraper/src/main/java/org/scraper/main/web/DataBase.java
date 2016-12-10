package org.scraper.main.web;

import org.apache.commons.collections.ListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBase implements IDataBase {
	
	private List<Site> sites = new ArrayList<>();
	
	private List<Domain> domains = new ArrayList<>();
	
	
	private List<Site> newSites = Collections.synchronizedList(new ArrayList<>());
	
	private List<Domain> newDomains = Collections.synchronizedList(new ArrayList<>());
	
	private Boolean gotSites = false;
	
	@Override
	public void getAll() {
		getAll(sites, domains);
		while (!gotSites) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void getAll(List<Site> sites, List<Domain> domains) {
		getSites(sites);
		getDomains(domains);
	}
	
	@Override
	public void postNew() {
		postAll(newSites, newDomains);
	}
	
	@Override
	public void postAll() {
		postAll(sites, domains);
	}
	
	private void postAll(List<Site> sites, List<Domain> domains) {
		postSites(sites);
		postDomains(domains);
	}
	
	protected void getSites(List<Site> sites) {
		PHPConnection.get(sites, PHPMethod.GET_SITES);
		gotSites = true;
	}
	
	protected  void getDomains(List<Domain> domains) {
		PHPConnection.get(domains, PHPMethod.GET_DOMAINS);
	}
	
	@Override
	public void postSites(List<Site> sites) {
		PHPConnection.post(sites, PHPMethod.POST_SITES);
	}
	
	protected  void postDomains(List<Domain> domains) {
		PHPConnection.post(domains, PHPMethod.POST_DOMAINS);
	}
	
	@Override
	public List<Site> getAllSites() {
		return ListUtils.sum(sites, newSites);
	}
	
	@Override
	public List<Domain> getAllDomains() {
		return ListUtils.sum(domains, newDomains);
	}
	
	
	@Override
	public void addSite(Site site) {
		if (!sites.contains(site)) {
			sites.add(site);
			newSites.add(site);
		}
	}
	
	@Override
	public void addDomain(Domain domain) {
		if (!domains.contains(domain)) {
			domains.add(domain);
			newDomains.add(domain);
		}
	}
}
