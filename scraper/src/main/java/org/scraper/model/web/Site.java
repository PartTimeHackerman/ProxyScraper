package org.scraper.model.web;

import org.scraper.model.managers.Checkable;
import org.scraper.model.scrapers.ScrapeType;

public class Site implements Checkable {
	
	private String address;
	
	private ScrapeType type;
	
	private String poster;
	
	private Integer sites = 0;
	
	public Site(String address, ScrapeType type) {
		this(address, type, "");
	}
	
	public Site(String address, ScrapeType type, String poster) {
		this.address = address;
		this.type = type;
		this.poster = poster;
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
	
	public String getPoster() {
		return poster;
	}
	
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	public Domain getDomain() {
		String domain = address.split("/")[2];
		return new Domain(domain, type);
	}
	
	public String getRoot() {
		String domain = getDomain().getDomain();
		return address.contains("https") ? "https://" + domain : "http://" + domain;
	}
	
	
	@Override
	public String getText() {
		return address;
	}
	
	@Override
	public String getParamOne() {
		return "todo";
	} //TODO
	
	@Override
	public String getParamTwo() {
		return sites != null ? sites.toString() : "";
	}
	
	@Override
	public String getParamThree() {
		return "Sites";
	}
	
	@Override
	public String toString() {
		return address + " " + type;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof Site && ((Site) o).address.equals(address));// && ((Site) o).type == type);
	}
	
	@Override
	public int hashCode() {
		return address.hashCode();
	}
	
	
}
