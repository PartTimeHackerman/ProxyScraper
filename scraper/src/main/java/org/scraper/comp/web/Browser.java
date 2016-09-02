package org.scraper.comp.web;

import mx4j.log.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

public class Browser {


	public static WebDriver getBrowser(String proxy, BrowserVersion version, String browser) {
		java.util.logging.Logger.getLogger(Browser.class.getName()).setLevel(java.util.logging.Level.OFF);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		if(proxy!=null) {
			org.openqa.selenium.Proxy browserProxy = new org.openqa.selenium.Proxy();
			browserProxy
					.setHttpProxy(proxy)
					.setSslProxy(proxy)
					.setFtpProxy(proxy)
					.setSocksProxy(proxy);
			capabilities.setCapability(CapabilityType.PROXY, browserProxy);
		}
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX+ "userAgent", version.ua());
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", false);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "javascriptEnabled", true);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "debug", false);

		capabilities.setCapability("marionette", true);
		capabilities.setPlatform(version.pl());
		capabilities.setBrowserName(version.nm());
		capabilities.setVersion(String.valueOf((int) (Math.random() * 48)));

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-agent=" + version.ua());
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		WebDriver driver;
		switch (browser){
			case "c": driver = new ChromeDriver(capabilities); break;
			case "p": driver = new PhantomJSDriver(capabilities); break;
			default: driver = new ChromeDriver(capabilities);
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}
}
