package org.scraper.comp.spider;

import org.scraper.comp.Globals;
import org.scraper.comp.scrapers.ScrapeType;
import org.scraper.comp.web.Site;

import java.util.List;

public class Spider {
	
	private LinksGather linksGather;
	
	private int timeout;
	
	private List<Site> gathered;
	
	private List<Site> checked;
	
	public static void main(String[] args) {
		Spider s = new Spider(new Site("https://incloak.com/proxy-list/", ScrapeType.UNCHECKED),2,0);
		s.gather();
		s.check(s.getGathered());
	}
	
	public Spider(Site site, int depth, int timeout){
		this.timeout = timeout;
		linksGather = new LinksGather(site, depth);
	}
	
	public List<Site> gatherAndCheck(){
		gather();
		return check(gathered);
	}
	
	public List<Site> gather(){
		linksGather.startGather();
		gathered = linksGather.getGathered();
		return gathered;
	}
	
	public List<Site> check(List<Site> sites){
		GatheredChecker gatheredChecker = new GatheredChecker(sites, timeout);
		gatheredChecker.check();
		checked = gatheredChecker.getChecked();
		return checked;
	}
	
	public List<Site> getGathered() {
		return gathered;
	}
	
	public List<Site> getChecked() {
		return checked;
	}
}
