package org.scraper.comp.web;

import mx4j.log.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.scraper.comp.Globals;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class Browser {

	private WebDriver browser;


	public static void main(String... args){
		Browser br = new Browser();
		br.click("http://tell-my-ip.com/","68.67.80.202:41271","socks");
	}

	public Browser(){
		browser = getBrowser(null, BrowserVersion.random(), "p");
	}

	private void click(String link, String proxy, String type) {

		changeProxy(proxy, type);
		browser.get(link);
		changeProxy("", "");
		//((TakesScreenshot) browser).getScreenshotAs(OutputType.FILE);
	}

	private void changeProxy(String proxy, String type){
		if(proxy.equals("")){
			String js = "phantom.setProxy(\"\",0);";
			((PhantomJSDriver) browser).executePhantomJS(js);
			return;
		}
		String[] ipPort = proxy.split(":");
		String proxyip = ipPort[0], proxyport = ipPort[1];


		String js = "phantom.setProxy(\"" + proxyip + "\"," + proxyport + ",\""+ (type.equals("socks")?"socks5":"http")+"\");";//, http, "", ""

		((PhantomJSDriver) browser).executePhantomJS(js);
	}

	public WebDriver getBrowser(String proxy, BrowserVersion version, String browser) {

		java.util.logging.Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.WARNING);
		java.util.logging.Logger.getLogger(PhantomJSDriver.class.getName()).setLevel(Level.WARNING);
		java.util.logging.Logger.getLogger("global").setLevel(Level.WARNING);

		DesiredCapabilities capabilities = new DesiredCapabilities();
		if (proxy != null) {
			org.openqa.selenium.Proxy browserProxy = new org.openqa.selenium.Proxy();
			browserProxy
					.setHttpProxy(proxy)
					.setSslProxy(proxy)
					.setFtpProxy(proxy)
					.setSocksProxy(proxy);
			capabilities.setCapability(CapabilityType.PROXY, browserProxy);
		}
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", version.ua());
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
		switch (browser) {
			case "c":
				driver = new ChromeDriver(capabilities);
				break;
			case "p":
				driver = new PhantomJSDriver(capabilities);
				break;
			default:
				driver = new ChromeDriver(capabilities);
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getBrowser() {
		return browser;
	}

	public void setBrowser(WebDriver browser) {
		this.browser = browser;
	}
}
