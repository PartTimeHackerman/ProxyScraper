package scraper.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scraper.MainLogger;
import scraper.web.BrowserVersion;
import scraper.data.Site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SitesScraper {
	
	private List<Site> sites;
	
	public SitesScraper() {
		this(new ArrayList<>());
	}
	
	public SitesScraper(List<Site> sites) {
		this.sites = sites;
	}
	
	public void scrapeSites() {
		String page = "&start=";
		Map<String, String> cookies = new HashMap<>();
		for (Lang lang : Lang.values()) {
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
						String address = siteLink.child(0).child(0).child(0).child(0).attr("href");
						
						if (address != null && address.length()>5 && !sites.contains(address))
							sites.add(new Site(address, ScrapeType.UNCHECKED));
					}
				} catch (IOException e) {
					MainLogger.log(this).error("Connection failed, url: {} error: {}", url, (e.getMessage()!=null?e.getMessage():"null"));
				}
			}
		}
	}
	
	private String getSearchURL(Lang lang) {
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
	
	public List<Site> getSites() {
		return sites;
	}
	
	enum Lang {
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
		
		Lang(String lang) {
			this.lang = lang;
		}
	}
	
}
