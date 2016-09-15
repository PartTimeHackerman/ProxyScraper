package org.scraper.comp.checker;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.scraper.comp.Globals;
import org.scraper.comp.Main;
import org.scraper.comp.Pool;
import org.scraper.comp.web.BrowserVersion;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProxyChecker {
	
	private int timeout;
	
	public static void main(String... args) {
		ProxyChecker pc = new ProxyChecker(3000);
		String socks = "68.67.80.202:41271";
		String elite = "45.79.87.67:8080";
		
		Consumer<String> check = prx -> {
			String[] result = pc.checkProxy(prx);
			if (result != null) {
				for (String s : result) System.out.print(s + " ");
			} else {
				Main.log.warn(prx + " broken");
			}
		};
		
		check.accept(socks);
		check.accept(elite);
	}
	
	public ProxyChecker(int timeout) {
		this.timeout = timeout;
	}
	
	public List<String[]> checkProxies(List<String> proxies) throws InterruptedException {
		Main.log.info("Checking list of size {}", proxies.size());
		List<Callable<String[]>> calls = new ArrayList<>();
		List<String[]> checked;
		
		 proxies.stream()
				.map(proxy ->
					calls.add(()-> checkProxy(proxy))
					)
				.collect(Collectors.toList());
		
		checked = Pool.sendTasks(calls);
		
		checked.removeIf(Objects::isNull);
		
		return checked;
	}
	
	public String[] checkProxy(String proxy) {
		// 0 - ip:port, 1 - type(socks, http, https), 2 - anonimity (transparent, elite, etc.), 3 - time
		String[] info = new String[4];
		info[0] = proxy;
		info[1]= "";
		
		String httpUrl = "http://www.google.cat/";//"http://www.wykop.pl/";
		String httpsUrl = "https://www.google.cat/";
		
		String ping = "http://absolutelydisgusting.ml/ping.php";
		
		String ip = proxy.split(":")[0];
		Integer port = Integer.parseInt(proxy.split(":")[1]);
		if (port >= 89595) {
			Main.log.warn("Proxy {}:{} port >= 89595", ip, port);
			return null;
		}
		
		Proxy http = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
		Proxy socks = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ip, port));
		
		String pingResopnse = pingProxy(http, ping);
		
		if (pingResopnse.equals("socks")) {
			pingResopnse = pingProxy(socks, ping);
			if (pingResopnse.contains("|")) {
				String[] responseInfo = pingResopnse.split("\\|");
				info[1] = "socks";
				info[2] = responseInfo[0];
				info[3] = responseInfo[1];
			}
		}else if (pingResopnse.contains("|")) {
			String[] responseInfo = pingResopnse.split("\\|");
			info[1] = "http";
			info[2] = responseInfo[0];
			info[3] = responseInfo[1];
			
			pingResopnse = pingProxy(http, httpsUrl);
			if (pingResopnse.contains("|")) {
				info[1] = "https";
			}
		}
		
		if(!info[1].equals("")){
			Main.log.info("Proxy {}:{} {} {} {}", ip, port, info[1], info[2], info[3]);
			Globals.handleChecked(info);
			return info;
		}else {
			Main.log.warn("Proxy {}:{} not working", ip, port);
			return null;
		}
	}
	
	private String pingProxy(Proxy proxy, String url) {
		Long counter = System.currentTimeMillis();
		try {
			Connection.Response response = Jsoup
					.connect(url)
					.userAgent(BrowserVersion.random().ua())
					.proxy(proxy)
					.ignoreContentType(true)
					.timeout(timeout)
					.execute();
			
			if (url.contains("https")) return "|";
			
			String[] typeAndTime = response.parse().text().split("\\|");
			return typeAndTime[0] + "|" + (System.currentTimeMillis() - counter);//(Long.parseLong(typeAndTime[1].replace(".",""))-counter);
			
		} catch (Exception e) {
			if (e.getMessage()!=null && e.getMessage().equals("Connection reset")) return "socks";
		}
		return "";
	}
}
