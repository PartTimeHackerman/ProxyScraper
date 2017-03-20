package org.scraper.main.data;

import org.scraper.main.scraper.ScrapeType;

import javax.persistence.*;

@Entity
@Table(name = "sites", schema = "public")
public class Site {
	
	@Id
	@Column(name = "address")
	private String address;
	
	@Column(name = "type")
	private ScrapeType type;
	
	@Column(name = "avgproxies")
	private int avgProxies = 0;
	
	@Column(name = "avgworking")
	private int avgWorkingProxies = 0;
	
	@Transient
	private boolean assignedAvgSites = false;
	
	@Transient
	private boolean assignedAvgWorking = false;
	
	public Site(){}
	
	public Site(String address, ScrapeType type) {
		setAddress(address);
		this.type = type;
	}
	
	public String getAddress() {
		return address;
	}
	
	public ScrapeType getType() {
		return type;
	}
	
	public void setType(ScrapeType type) {
		this.type = type;
	}
	
	public Domain getDomain() {
		String domain = address.split("/")[2];
		return new Domain(domain, type);
	}
	
	public String getRoot() {
		String domain = getDomain().getDomainString();
		return address.contains("https") ? "https://" + domain : "http://" + domain;
	}
	
	private void setAddress(String address){
		this.address = address.substring(0,5).equals("https")
				? address
				: address.substring(0,4).equals("http")
					? address
					: "http://" + address + "/";
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof Site && ((Site) o).address.equals(address));
	}
	
	@Override
	public int hashCode() {
		return address.hashCode();
	}
	
	@Override
	public String toString(){
		StringBuilder siteString = new StringBuilder(address);
		siteString.append(" Type: ").append(type);
		siteString.append(" Avg. sites: ").append(isAvgSitesAssigned() ? avgProxies : "UNKNOWN");
		siteString.append(" Avg. working: ").append(isAvgWorkingAssigned() ? avgWorkingProxies : "UNKNOWN");
		return siteString.toString();
	}
	
	
	public Integer getAvgWorkingProxies() {
		return avgWorkingProxies;
	}
	
	public void setAvgWorkingProxies(Integer avgWorkingProxies) {
		this.avgWorkingProxies = avgWorkingProxies;
		assignedAvgWorking = true;
	}
	
	public Integer getAvgProxies() {
		return avgProxies;
	}
	
	public void setAvgProxies(Integer avgProxies) {
		this.avgProxies = avgProxies;
		assignedAvgSites = true;
	}
	
	public Boolean isAvgSitesAssigned() {
		return assignedAvgSites || avgProxies > 0;
	}
	
	public Boolean isAvgWorkingAssigned() {
		return assignedAvgWorking || avgWorkingProxies > 0;
	}
}
