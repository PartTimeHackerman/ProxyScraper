package org.scraper.main.web;


import org.openqa.selenium.Platform;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum BrowserVersion {

	FIREFOX_WIN("Mozilla/5.0 (Windows NT 6.2; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0", Platform.WIN10, "Mozilla Firefox"),
	FIREFOX_MAC("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:21.0) Gecko/20100101 Firefox/21.0", Platform.YOSEMITE, "Mozilla Firefox"),
	FIREFOX_UB("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:21.0) Gecko/20130331 Firefox/21.0", Platform.LINUX, "Mozilla Firefox"),

	CHROME_WIN("Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36", Platform.WIN10, "Chrome"),
	CHROME_MAC("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.93 Safari/537.36", Platform.MAVERICKS, "Chrome"),
	CHROME_UB("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.11 (KHTML, like Gecko) Ubuntu/11.10 Chromium/27.0.1453.93 Chrome/27.0.1453.93 Safari/537.36", Platform.LINUX, "Chrome"),

	OPERA_WIN("Opera/9.80 (Windows NT 6.1; WOW64; U; en) Presto/2.10.229 Version/11.62", Platform.WIN8_1, "Opera"),
	OPERA_MAC("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.9.168 Version/11.52", Platform.EL_CAPITAN, "Opera"),

	SAFARI_WIN("Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27", Platform.XP, "Safari"),
	SAFARI_MAC("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_6; en-US) AppleWebKit/533.20.25 (KHTML, like Gecko) Version/5.0.4 Safari/533.20.27", Platform.MOUNTAIN_LION, "Safari"),

	IE11("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586", Platform.WIN10, "Internet Explorer"),
	IE10("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586", Platform.WIN8, "Internet Explorer"),
	IE9("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586", Platform.WINDOWS, "Internet Explorer");

	private String userAgent;
	private Platform platform;
	private String name;

	private static final List<BrowserVersion> browserVersions = Collections.unmodifiableList(Arrays.asList(values()));

	BrowserVersion(String userAgent, Platform platform, String name) {
		this.userAgent = userAgent;
		this.platform = platform;
		this.name = name;
	}

	public String ua() { return userAgent; }

	public Platform pl(){ return platform; }

	public String nm(){ return name; }

	public static BrowserVersion random(){
		return browserVersions.get((int)(Math.random()*browserVersions.size()));
	}


}
