package scraper.data;

import scraper.scraper.ScrapeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "domains")
public class Domain {
	
	@Id
	@Column(name = "domain")
	private String domain;
	
	@Column(name = "type")
	private ScrapeType type;
	
	public Domain(){}
	
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
