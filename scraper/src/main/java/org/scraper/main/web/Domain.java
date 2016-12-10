package org.scraper.main.web;

import org.scraper.main.scraper.ScrapeType;

public class Domain {
	
	private String domain;
	
	private ScrapeType type;
	
	public Domain(String domain, ScrapeType type){
		this.domain = domain;
		this.type = type;
	}
	
	public String getDomainString() {
		return domain;
	}
	
	public ScrapeType getType() {
		return type;
	}
	
	@Override
	public boolean equals(Object o){
		return (o instanceof Domain && ((Domain)o).domain.equals(domain));
	}
}
