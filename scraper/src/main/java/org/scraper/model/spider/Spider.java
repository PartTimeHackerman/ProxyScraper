package org.scraper.model.spider;

import org.scraper.model.Main;
import org.scraper.model.scrapers.ScrapeType;
import org.scraper.model.web.Site;

import java.util.List;

public class Spider {
	
	private LinksGather linksGather;
	
	public static void main(String[] args) {
		Spider s = new Spider(2);
		List<Site> ss = s.gather(new Site("http://gatherproxy.com/proxylist/anonymity/?t=Elite", ScrapeType.UNCHECKED));
		
		ss.forEach( l -> Main.log.info(l));
	}
	
	public Spider(int depth){
		linksGather = new LinksGather(depth);
	}
	
	
	public List<Site> gather(Site site){
		return linksGather.gather(site);
	}
}
