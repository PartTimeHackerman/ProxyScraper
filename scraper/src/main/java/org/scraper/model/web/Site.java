package org.scraper.model.web;

import org.scraper.model.scrapers.ScrapeType;

public class Site {
	
	private String address;
	
	private ScrapeType type;
	
	private Integer avgSites = 0;
	
	private Integer avgWorking = 0;
	
	private Boolean assignedAvgSites = false;
	
	private Boolean assignedAvgWorking = false;
	
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
		siteString.append(" Avg. sites: ").append(assignedAvgSites ? avgSites : "UNKNOWN");
		siteString.append(" Avg. working: ").append(assignedAvgWorking ? avgWorking : "UNKNOWN");
		return siteString.toString();
	}
	
	
	public Integer getAvgWorking() {
		return avgWorking;
	}
	
	public void setAvgWorking(Integer avgWorking) {
		this.avgWorking = avgWorking;
		assignedAvgWorking = true;
	}
	
	public Integer getAvgSites() {
		return avgSites;
	}
	
	public void setAvgSites(Integer avgSites) {
		this.avgSites = avgSites;
		assignedAvgSites = true;
	}
	
	public Boolean isAvgSitesAssigned() {
		return assignedAvgSites;
	}
	
	public Boolean isAvgWorkingAssigned() {
		return assignedAvgWorking;
	}
}
