package org.scraper.model.web;

import org.scraper.model.scrapers.ScrapeType;

public class Site {
	
	private String address;
	
	private ScrapeType type;
	
	private Integer avgSites = -1;
	
	private Integer avgWorking = -1;
	
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
		return (o instanceof Site && ((Site) o).address.equals(address));// && ((Site) o).type == type);
	}
	
	@Override
	public int hashCode() {
		return address.hashCode();
	}
	
	
	public Integer getAvgWorking() {
		return avgWorking != null ? avgWorking : 0;
	}
	
	public void setAvgWorking(Integer avgWorking) {
		this.avgWorking = avgWorking;
	}
	
	public Integer getAvgSites() {
		return avgSites != null ? avgSites : 0;
	}
	
	public void setAvgSites(Integer avgSites) {
		this.avgSites = avgSites;
	}
}
