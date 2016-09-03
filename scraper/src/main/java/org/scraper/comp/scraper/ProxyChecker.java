package org.scraper.comp.scraper;

import org.jsoup.Jsoup;
import org.scraper.comp.web.BrowserVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ProxyChecker {

	public String checkProxy(String proxy, Integer timeout) {
		String httpUrl = "http://www.google.cat/";//"http://www.wykop.pl/";
		String httpsUrl = "https://www.google.cat/";

		String ping = "http://absolutelydisgusting.ml/ping.php";

		String ip = proxy.split(":")[0];
		Integer port = Integer.parseInt(proxy.split(":")[1]);

		Proxy http = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
		Proxy socks = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ip, port));

		String pingResopnse = pingProxy(http, ping, timeout);

		if(pingResopnse.equals("socks"))
				pingResopnse = pingProxy(socks, ping, timeout);
		if (pingResopnse==null)
			return null;
		return pingResopnse;
	}

	private  String pingProxy(Proxy proxy, String url, Integer timeout){
		Long counter = System.currentTimeMillis();
		try {
			String[] typeAndTime = Jsoup
					.connect(url)
					.userAgent(BrowserVersion.random().ua())
					.proxy(proxy)
					.ignoreContentType(true)
					.timeout(timeout)
					.execute()
					.parse()
					.text()
					.split("\\|");

			String type = typeAndTime[0];
			Integer time = (int)(Integer.parseInt(typeAndTime[1]) - counter);

			return proxy + type + "|" + time;
		} catch (Exception e) {
			if(e instanceof SocketException) return "socks";
		}
		return null;
	}
}
