package org.scraper.comp.scrapers;


import org.scraper.comp.Main;
import org.scraper.comp.Pool;
import org.scraper.comp.checker.ProxyChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegexMatcher {
	
	public static List<String> match(String text) throws InterruptedException {
		return match(text, null);
	}
	public static List<String> match(String text, ProxyChecker checker) throws InterruptedException {
		Pattern ipRegex = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
		Pattern portRegex = Pattern.compile("[0-9]{1,5}");
		Pattern ipPort = Pattern.compile(ipRegex + ":" + portRegex);
		
		List<String> proxy = new CopyOnWriteArrayList<>();
		List<String> ip = new ArrayList<>();
		
		Matcher ipMatcher = ipRegex.matcher(text);
		Matcher portMatcher = portRegex.matcher(text);
		
		String tempProxy;
		while (ipMatcher.find()) {
			tempProxy = ipMatcher.group();
			if (portMatcher.find(ipMatcher.end()) && !ip.contains(tempProxy)) {
				ip.add(tempProxy);
				tempProxy += ":" + portMatcher.group();
				if (tempProxy.matches(ipPort.toString())) {
					proxy.add(tempProxy);
				}
			}
		}
		
		
		if (checker!=null) {
			return checker.checkProxies(proxy)
			.stream()
			.map(arr -> arr[0])
			.collect(Collectors.toList());
		}
		//proxy.add(tempProxy); //Only add to textarea in future
		//log.info(tempProxy + " " + check[0] + " " + check[1] + " " + check[2]);}
		return proxy;
	}
}
