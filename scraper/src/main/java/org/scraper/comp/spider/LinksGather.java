package org.scraper.comp.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.scraper.comp.Main;
import org.scraper.comp.scrapers.ScrapeType;
import org.scraper.comp.web.BrowserVersion;
import org.scraper.comp.web.Domain;
import org.scraper.comp.web.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LinksGather {
	
	private Site site;
	
	private String root;
	
	private int depth;
	
	private List<String> gathered = new ArrayList<>();
	
	public static void main(String[] args) {
		LinksGather lg = new LinksGather(new Site("http://proxylist.hidemyass.com/", ScrapeType.UNCHECKED), 2);
		lg.startGather();
		List<Site> sites = lg.getGathered();
		sites.forEach(site -> Main.log.info(site.getAddress()));
	}
	
	public LinksGather(Site site, int depth) {
		this.site = site;
		root = site.getAddress();
		this.depth = depth;
	}
	
	public void startGather() {
		List<String> newLinks = new ArrayList<>();
		List<String> tempLinks = new ArrayList<>();
		newLinks.add(root);
		
		IntStream
				.range(0, depth)
				.forEach(i -> {
					newLinks.forEach(link -> {
						List<String> got = gatherLinks(link);
						gathered.addAll(got);
						tempLinks.addAll(got);
					});
					newLinks.clear();
					newLinks.addAll(tempLinks.stream().distinct().collect(Collectors.toList()));
					tempLinks.clear();
				});
	}
	
	private List<String> gatherLinks(String url) {
		Document document = connect(url);
		
		if (document == null)
			return new ArrayList<>();
		
		Elements redirects = document.getElementsByAttribute("href");
		
		return redirects
				.stream()
				.map(redirect -> redirect.attr("href"))
				.filter(link -> link != null && !link.equals(""))
				.map(link -> link.charAt(0) == '/' ? site.getRoot() + link : link)
				.map(link -> link.contains("#") ? link.split("#")[0] : link)
				.filter(link -> link.length() > 4
						&& link.contains(root)
						&& !gathered.contains(link)
						&& link.substring(0, 4).equals("http")
						&& !link.endsWith(".css"))
				.distinct()
				.collect(Collectors.toList());
	}
	
	private Document connect(String url) {
		Document document = null;
		try {
			document = Jsoup
					.connect(url)
					.userAgent(BrowserVersion.random().ua())
					.timeout(5000)
					.get();
			
		} catch (IOException e) {
			Main.log.warn("Gather {} connection failed!", url);
		}
		return document;
	}
	
	public List<Site> getGathered() {
		return gathered
				.stream()
				.map(link -> new Site(link, ScrapeType.UNCHECKED))
				.collect(Collectors.toList());
	}
}
