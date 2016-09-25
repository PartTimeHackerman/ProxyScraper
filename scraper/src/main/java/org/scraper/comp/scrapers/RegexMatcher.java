package org.scraper.comp.scrapers;


import org.scraper.comp.Globals;
import org.scraper.comp.Main;
import org.scraper.comp.Pool;
import org.scraper.comp.Proxy;
import org.scraper.comp.checker.ProxyChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexMatcher {
	
	private static Pattern ipPattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	private static Pattern portPattern = Pattern.compile("[0-9]{1,5}");
	private static Pattern ipPortPattern = Pattern.compile(ipPattern + ":" + portPattern);
	private static List<Proxy> proxys;
	
	
	public static List<Proxy> match(String text) throws InterruptedException {
		return match(text, null);
	}
	
	public static List<Proxy> match(String text, ProxyChecker checker) {
		List<Proxy> proxy;
		
		proxy = patternMatch(text, ipPattern, portPattern);
		if (proxy.size() < 5)
			proxy = patternMatch(text, portPattern, ipPattern);
		
		if (checker != null) {
			proxy = checker.checkProxies(proxy)
					.stream()
					.collect(Collectors.toList());
		}
		return proxy;
	}
	
	private static List<Proxy> patternMatch(String text, Pattern firstPattern, Pattern secondPattern) {
		List<Proxy> proxy = new ArrayList<>();
		List<String> ips = new ArrayList<>();
		boolean reversed = false;
		if (firstPattern == portPattern) reversed = true;
		
		int maxDistance = 10;
		
		Matcher firstMatcher = firstPattern.matcher(text);
		Matcher secondMatcher = secondPattern.matcher(text);
		
		String temp1, temp2, tempProxy;
		while (firstMatcher.find()) {
			temp1 = firstMatcher.group();
			if (secondMatcher.find(firstMatcher.end())
					&& (secondMatcher.end() - secondMatcher.group().length()) - firstMatcher.end() < maxDistance) {
				
				temp2 = secondMatcher.group();
				
				String ip;
				if (reversed) {
					ip = temp2;
					tempProxy = temp2 + ":" + temp1;
				} else {
					ip = temp1;
					tempProxy = temp1 + ":" + temp2;
				}
				
				if (tempProxy.matches(ipPortPattern.toString()) && !ips.contains(ip)) {
					ips.add(ip);
					Proxy prx = new Proxy(tempProxy);
					if (!proxys.contains(prx)) proxy.add(prx);
				}
			}
		}
		return proxy;
	}
	
	public static void setProxy(List<Proxy> aproxy) {
		proxys = aproxy;
	}
}
