package org.scraper.main.data;

import org.scraper.main.Proxy;
import org.scraper.main.data.dao.DomainDAO;
import org.scraper.main.scraper.ScrapeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DomainsRepo implements IRepository<Domain> {
	
	private final List<Domain> domains = Collections.synchronizedList(new ArrayList<>());
	
	private final DomainDAO domainDAO = new DomainDAO();
	
	public DomainsRepo(){
		//domains.addAll(domainDAO.fetchAll());
	}
	
	public boolean contains(Domain domain){
		return domains.contains(domain);
	}
	
	public ScrapeType getDomainScrapeType(Domain domain){
		return domains.get(domains.indexOf(domain)).getType();
	}
	
	@Override
	public List<Domain> getAll() {
		return domains;
	}
	
	@Override
	public void add(Domain domain) {
		domains.add(domain);
	}
	
	@Override
	public void addAll(Collection<Domain> domains) {
		this.domains.addAll(domains);
	}
	
	@Override
	public void fetchAllFromDB() {
		addAll(domainDAO.fetchAll());
	}
	
	@Override
	public void postOne(Domain domain) {
		domainDAO.post(domain);
	}
	
	@Override
	public void postAllToDB() {
		domainDAO.postAll(domains);
	}
}
