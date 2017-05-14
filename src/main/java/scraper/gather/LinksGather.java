package scraper.gather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import scraper.data.SitesRepo;
import scraper.scraper.ScrapeType;
import scraper.web.BrowserVersion;
import scraper.web.LanguageCheck;
import scraper.data.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LinksGather extends Observable {
	
	private Integer depth;
	private SitesRepo sitesRepo;
	
	public LinksGather(Integer depth) {
		this.depth = depth;
	}
	
	public LinksGather(SitesRepo sitesRepo, Integer depth) {
		this(depth);
		this.sitesRepo = sitesRepo;
	}
	
	public List<Site> gather(Site site) {
		List<String> start = startGather(site);
		List<Site> gathered = start
				.stream()
				.map(link -> new Site(link, ScrapeType.UNCHECKED))
				.collect(Collectors.toList());
		
		setChanged();
		notifyObservers(gathered);
		
		return gathered;
	}
	
	private List<String> startGather(Site site) {
		List<String> links = new ArrayList<>();
		List<String> newLinks = new ArrayList<>();
		List<String> tempLinks = new ArrayList<>();
		
		links.add(site.getAddress());
		newLinks.add(site.getAddress());
		
		IntStream.range(0, depth)
				.forEach(i -> {
					newLinks.forEach(link -> {
						List<String> got = gatherLinks(new Site(link, null), links);
						links.addAll(got);
						tempLinks.addAll(got);
					});
					newLinks.clear();
					newLinks.addAll(tempLinks.stream()
											.distinct()
											.collect(Collectors.toList()));
					tempLinks.clear();
				});
		
		if (sitesRepo != null)
			removeDuplicates(links);
		return links;
	}
	
	private List<String> gatherLinks(Site site, List<String> all) {
		String address = site.getAddress();
		String root = site.getRoot();
		
		Document document = connect(address);
		
		if (document == null)
			return new ArrayList<>();
		
		Elements redirects = document.getElementsByAttribute("href");
		
		return redirects
				.stream()
				.map(redirect ->
							 redirect.attr("href"))
				.filter(link ->
								link != null && link.length() > 1)
				.map(link ->
							 link.charAt(0) == '/' ? root + link : link)
				.map(link ->
							 link.contains("#") ? link.split("#")[0] : link)
				.map(link ->
							 link.endsWith("/") ? link.substring(0, link.length() - 1) : link)
				.filter(link ->
								link.length() > 4
										&& !isLinkToFile(link)
										&& link.substring(0, 4).equals("http")
										&& !link.matches(".*\\.[a-z]{3}\\?.*")
										&& link.contains(root)
										&& !all.contains(link)
										&& !link.equals(address)
										&& !LanguageCheck.isFromOtherLang(link, all))
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
			
		} catch (IOException ignored) {
		}
		return document;
	}
	
	public List<Site> gatherSites(Collection<Site> sites) {
		List<List<Site>> gatheredSitesLists = new ArrayList<>();
		
		sites.forEach(site ->
							  gatheredSitesLists.add(gather(site)));
		
		List<Site> gatheredSites = new ArrayList<>();
		
		gatheredSitesLists.forEach(gatheredSites::addAll);
		return gatheredSites;
	}
	
	private void removeDuplicates(List<String> adresses) {
		adresses.removeIf(siteAddress ->
								  sitesRepo.contains(siteAddress));
	}
	
	private boolean isLinkToFile(String link) {
		return link.endsWith(".exe")
				|| link.endsWith(".css")
				|| link.endsWith(".png")
				|| link.endsWith(".jpg");
	}
	
	public Integer getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
}
