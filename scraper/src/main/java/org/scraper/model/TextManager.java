package org.scraper.model;

import org.apache.commons.collections.ArrayStack;
import org.scraper.model.scraper.RegexMatcher;
import org.scraper.model.scraper.ScrapeType;
import org.scraper.model.web.Site;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextManager {
	
	private List<Text> textArea = new ArrayList<>();
	private ProxyManager proxyManager;
	private SitesManager sitesManager;
	
	private String proxyPattern = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}.*[0-9]";
	private String sitePattern = ".*\\.[a-z]{2,3}.*";
	
	public TextManager(ProxyManager proxyManager, SitesManager sitesManager){
		this.proxyManager = proxyManager;
		this.sitesManager = sitesManager;
	}
	
	public void addToText(List<String> text){
		text.forEach(line -> {
			if(line.matches(proxyPattern)) {
				Proxy proxy = RegexMatcher.match(line).get(0);
				proxy = proxyManager.getIfPresent(proxy);
				textArea.add(proxy);
			}
			if(line.matches(sitePattern)){
				line = (line.contains("http")) ? line : "http://" + line;
				Site site = new Site(line, ScrapeType.UNCHECKED);
				site= sitesManager.getIfPresent(site);
				textArea.add(site);
			}
		});
	}
	
	public List<Proxy> getProxy() {
		return textArea.stream().filter(proxy -> proxy instanceof Proxy).map(proxy -> (Proxy)proxy).collect(Collectors.toList());
	}
	
	
	public List<Site> getSites() {
		return textArea.stream().filter(site -> site instanceof Site).map(site -> (Site)site).collect(Collectors.toList());
	}
}
