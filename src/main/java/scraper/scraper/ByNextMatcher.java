package scraper.scraper;


import scraper.Proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByNextMatcher implements IProxyMatcher {
	
	@Override
	public List<Proxy> match(String text) {
		List<Proxy> proxy;
		
		proxy = patternMatch(text, false);
		if (proxy.size() < 1)
			proxy = patternMatch(text, true);
		
		return proxy;
	}
	
	private List<Proxy> patternMatch(String text, boolean reversed) {
		List<Proxy> proxy = new ArrayList<>();
		List<String> ips = new ArrayList<>();
		
		Pattern firstPattern;
		Pattern secondPattern;
		
		if(reversed){
			firstPattern = portPattern;
			secondPattern = ipPattern;
		}
		else {
			firstPattern = ipPattern;
			secondPattern = portPattern;
		}
		
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
					proxy.add(prx);
				}
			}
		}
		return proxy;
	}
	
	@Override
	public  Proxy matchOne(String text) {
		List<Proxy> proxies;
		if (!(proxies = patternMatch(text, false)).isEmpty()) {
			return proxies.get(0);
		} else {
			if (!(proxies = patternMatch(text, true)).isEmpty())
				return proxies.get(0);
		}
		return null;
	}
}
