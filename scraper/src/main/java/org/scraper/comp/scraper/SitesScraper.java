package org.scraper.comp.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.scraper.comp.other.PHP;
import org.scraper.comp.web.Browser;
import org.scraper.comp.web.BrowserVersion;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SitesScraper {

	private List<Address> addrs;

	private List<Address> newAddrs = new ArrayList<>();

	public SitesScraper() {
		addrs = (List<Address>) PHP.get(PostGetPHP.GET_ADDRS);
	}

	public static void main(String... args){
		SitesScraper ss = new SitesScraper();
		System.out.println(ss.getSearchURL(GGLang.PL));
		ss.scrapeSites();
		PHP.phpPost(PostGetPHP.POST_ADDRS,PHP.gson.toJson(ss.newAddrs));
		List<Integer> ints = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			try {
				Jsoup.connect("http://www.wykop.pl/").proxy("87.98.239.19", 8080).timeout(10000).userAgent("Mozilla").execute();
				ints.add(i);
				System.out.println(i+" 11111111111111111");
			} catch (IOException e) {
				System.out.println(i+" 0");
			}
		}
	}

	private String getSearchURL(GGLang lang){
		String[] term = lang.lang.split(" ");
		StringBuilder url = new StringBuilder("https://www.google.com/search?hl=en&as_q=");
		for (int i = 0; i < term.length; i++) {
			if (i>0) url.append("+");
			url.append(term[i]);
		}
		url.append("&as_epq=&as_oq=&as_eq=&as_nlo=&as_nhi=&lr=lang_");
		url.append(lang.toString().toLowerCase());
		url.append("&cr=&as_qdr=all&as_sitesearch=&as_occt=any&safe=images&as_filetype=&as_rights=");
		return url.toString();
	}

	private void scrapeSites(){
		String page = "&start=";
		Map<String, String> cookies = new HashMap<>();
		for (GGLang lang : GGLang.values()){
			String url = getSearchURL(lang);
			for (int i = 0; i < 50; i+=10) {
				url+=page+i;
				try {
					Connection.Response response = Jsoup.connect(url)
							.userAgent(BrowserVersion.CHROME_WIN.ua())
							.method(Connection.Method.GET)
							.cookies(cookies)
							.execute();
					cookies.putAll(response.cookies());

					Elements sitesLinks = response.parse().getElementsByAttributeValue("class","g");
					for (Element siteLink : sitesLinks){
						String address = siteLink.child(0).child(0).child(0).attr("href");
						System.out.println(address);

						if(!addrs.stream().filter(e->e.getAddress().equals(address)).findFirst().isPresent())
						newAddrs.add(new Address(address, Address.Type.UNCHECKED,InetAddress.getLocalHost().getHostAddress() + " " + System.getProperty("user.name")));
					}
				} catch (IOException e) {
					System.out.println(e.toString());
				}
			}
		}
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
			this.lang=lang;
		}
	}

}
