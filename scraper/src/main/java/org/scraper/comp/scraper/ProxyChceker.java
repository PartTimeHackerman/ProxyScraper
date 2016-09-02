package org.scraper.comp.scraper;

import org.jsoup.Jsoup;
import org.scraper.comp.web.BrowserVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class ProxyChceker {

	public static String checkProxy(String proxy, Integer timeout) {
		String httpUrl = "http://www.google.cat/";//"http://www.wykop.pl/";
		String httpsUrl = "https://www.google.cat/";

		String ping = "http://absolutelydisgusting.ml/ping.php";

		String ip = proxy.split(":")[0];
		Integer port = Integer.parseInt(proxy.split(":")[1]);

		Proxy http = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
		Proxy socks = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ip, port));

		try {
			//Socket uc = new Socket(socks);
			//uc.connect(new InetSocketAddress(ping, 8080));

			URL pingUrl = new URL(ping);
			URLConnection uc = pingUrl.openConnection(socks);

			//HttpURLConnection uc = (HttpURLConnection) pingUrl.openConnection(socks);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					uc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Long counter;

		try {
			counter = System.currentTimeMillis();
			String[] typeAndTime = Jsoup
					.connect(ping)
					.userAgent(BrowserVersion.random().ua())
					.proxy(socks)
					.ignoreContentType(true)
					.timeout(timeout)
					.execute()
					.parse()
					.text()
					.split("\\|");

			String type = typeAndTime[0];
			Integer time = Math.toIntExact(Integer.parseInt(typeAndTime[1]) - counter);

			return proxy + type + "|" + time;
		} catch (Exception e) {
			counter = System.currentTimeMillis();
			try {
				Jsoup
						.connect(ping)
						.userAgent(BrowserVersion.random().ua())
						.proxy(socks)
						//.ignoreContentType(true)
						.timeout(timeout)
						.followRedirects(true)
						.execute();
				return proxy + "|http" + "|" + (System.currentTimeMillis() - counter);
			} catch (Exception e1) {
				counter = System.currentTimeMillis();
				try {
					Jsoup
							.connect(httpUrl)
							.userAgent(BrowserVersion.random().ua())
							.proxy(socks)
							.ignoreContentType(true)
							.timeout(timeout)
							.execute();
					return proxy + "|socks" + "|" + (System.currentTimeMillis() - counter);
				} catch (Exception e2) {
					return proxy;
				}
			}
		}
	}
}
