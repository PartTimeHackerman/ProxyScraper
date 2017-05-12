package scraper.scraper;

import scraper.Proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class PairMatcher implements IProxyMatcher {
	
	@Override
	public List<Proxy> match(String text) {
		List<Proxy> proxy = new ArrayList<>();
		List<String> ips = new ArrayList<>();
		List<Integer> ports = new ArrayList<>();
		
		
		Matcher firstMatcher = ipPattern.matcher(text);
		Matcher secondMatcher = portPattern.matcher(text);
		
		while (firstMatcher.find()) {
			ips.add(firstMatcher.group());
		}
		
		while (secondMatcher.find()) {
			Integer port = Integer.parseInt(secondMatcher.group());
			ports.add(port);
		}
		
		for (int i = 0; i < ips.size(); i++) {
			proxy.add(new Proxy(ips.get(i), ports.get(i)));
		}
		
		return proxy;
	}
	
	@Override
	public  Proxy matchOne(String text) {
		List<Proxy> proxies;
		if (!(proxies = match(text)).isEmpty()) {
			return proxies.get(0);
		} else {
			if (!(proxies = match(text)).isEmpty())
				return proxies.get(0);
		}
		return null;
	}
}
