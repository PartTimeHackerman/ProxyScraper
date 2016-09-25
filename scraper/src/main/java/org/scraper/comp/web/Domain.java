package org.scraper.comp.web;

import org.scraper.comp.scrapers.ScrapeType;

public class Domain {
	
	private String domain;
	
	private ScrapeType type;
	
	public Domain(String domain, ScrapeType type){
		this.domain = domain;
		this.type = type;
	}
	
	public String getDomain() {
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
