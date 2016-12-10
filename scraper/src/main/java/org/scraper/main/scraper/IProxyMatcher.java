package org.scraper.main.scraper;

import org.scraper.main.Proxy;

import java.util.List;
import java.util.regex.Pattern;

public interface IProxyMatcher {
	
	Pattern ipPattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	Pattern portPattern = Pattern.compile("(?<!\\.|\\d)[0-9]{1,5}(?!\\.|\\d)");
	Pattern ipPortPattern = Pattern.compile(ipPattern + ":" + portPattern);
	
	List<Proxy> match(String text);
	
	Proxy matchOne(String text);
}
