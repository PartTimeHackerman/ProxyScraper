package org.scraper.comp.scrapers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.scraper.comp.Globals;
import org.scraper.comp.Main;
import org.scraper.comp.checker.ProxyChecker;
import org.scraper.comp.web.Address;
import org.scraper.comp.web.PHP;
import org.scraper.comp.web.BrowserVersion;
import org.scraper.comp.web.PHPMethod;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SitesScraper {
	
	private List<Address> addrs;
	
	private List<Address> newAddrs = new ArrayList<>();
	
	private static ProxyScraper scraper = new ProxyScraper(false);
	
	private static ProxyChecker checker = new ProxyChecker(3000);
	
	public SitesScraper() {
		addrs = Globals.getSites();
	}
	
	public static void main(String... args) throws Exception {
		Globals.init(100, 1000, true);
		SitesScraper ss = new SitesScraper();
		try {
			ss.getType(new Address("http://proxylist.hidemyass.com/", Address.Type.UNCHECKED, ""));
		} catch (InterruptedException e) {
			Main.log.warn("Thread interrupted {}", e);
		}
		
		System.out.println(ss.getSearchURL(GGLang.PL));
		ss.scrapeSites();
		PHP.phpPost(PHPMethod.POST_ADDRS, PHP.gson.toJson(ss.newAddrs));
		List<Integer> ints = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			try {
				Jsoup.connect("http://www.wykop.pl/").proxy("87.98.239.19", 8080).timeout(10000).userAgent("Mozilla").execute();
				ints.add(i);
				System.out.println(i + " 11111111111111111");
			} catch (IOException e) {
				System.out.println(i + " 0");
			}
		}
	}
	
	private String getSearchURL(GGLang lang) {
		String[] term = lang.lang.split(" ");
		StringBuilder url = new StringBuilder("https://www.google.com/search?hl=en&as_q=");
		for (int i = 0; i < term.length; i++) {
			if (i > 0) url.append("+");
			url.append(term[i]);
		}
		url.append("&as_epq=&as_oq=&as_eq=&as_nlo=&as_nhi=&lr=lang_");
		url.append(lang.toString().toLowerCase());
		url.append("&cr=&as_qdr=all&as_sitesearch=&as_occt=any&safe=images&as_filetype=&as_rights=");
		return url.toString();
	}
	
	private void scrapeSites() {
		String page = "&start=";
		Map<String, String> cookies = new HashMap<>();
		for (GGLang lang : GGLang.values()) {
			String url = getSearchURL(lang);
			for (int i = 0; i < 50; i += 10) {
				url += page + i;
				try {
					Connection.Response response = Jsoup.connect(url)
							.userAgent(BrowserVersion.CHROME_WIN.ua())
							.method(Connection.Method.GET)
							.cookies(cookies)
							.execute();
					cookies.putAll(response.cookies());
					
					Elements sitesLinks = response.parse().getElementsByAttributeValue("class", "g");
					for (Element siteLink : sitesLinks) {
						String address = siteLink.child(0).child(0).child(0).attr("href");
						System.out.println(address);
						
						if (! addrs.stream().filter(e -> e.getAddress().equals(address)).findFirst().isPresent())
							newAddrs.add(new Address(address, Address.Type.UNCHECKED, InetAddress.getLocalHost().getHostAddress() + " " + System.getProperty("user.name")));
					}
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			}
		}
	}
	
	public Address.Type getType(Address address) throws InterruptedException, IOException {
		String site = address.getAddress();
		int minAll = 10, minWorking = 0;
		double minWoringPrecent = 0.2;
		
		Address.Type type;
		
		Main.log.info("Getting {} scrapng method", site);
		
		if (workingPrecent(ScrapeType.NORMAL, site, minAll, minWorking) < minWoringPrecent) {
			if (workingPrecent(ScrapeType.CSS, site, minAll, minWorking) < minWoringPrecent) {
				type = (workingPrecent(ScrapeType.OCR, site, minAll, minWorking) < minWoringPrecent) ? Address.Type.BLACK : Address.Type.OCR;
			} else {
				type = Address.Type.CSS;
			}
		} else {
			type = Address.Type.NORMAL;
		}
		return type;
	}
	
	private double workingPrecent(ScrapeType type, String url, int minAll, int minWorking) throws IOException, InterruptedException {
		List<String> prxs = scraper.scrape(type, url);
		int all = prxs.size();
		int working = checker.checkProxies(prxs).size();
		
		return all > minAll && working > minWorking ? (double) working / (double) all : 0D;
	}
	
	public List<Address> getAddrs() {
		return addrs;
	}
	
	public void setAddrs(List<Address> addrs) {
		this.addrs = addrs;
	}
	
	public List<Address> getNewAddrs() {
		return newAddrs;
	}
	
	public void setNewAddrs(List<Address> newAddrs) {
		this.newAddrs = newAddrs;
	}
	
	enum GGLang {
		EN("proxy list"),
		ES("lista de proxy"),
		RU("список прокси"),
		FR("proxy list"),
		DE("Proxy-Liste"),
		PL("lista proxy"),
		JP("プロキシリスト"),
		KO("프록시 목록"),
		CN("代理列表");
		public String lang;
		
		GGLang(String lang) {
			this.lang = lang;
		}
	}
	
}
