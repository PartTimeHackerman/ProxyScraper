package org.scraper.model.web;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.scraper.model.Main;
import org.scraper.model.Proxy;

public class Browser {
	
	private WebDriver browser;
	
	
	public static void main(String... args) {
		Browser br = new Browser();
		//br.click("http://tell-my-ip.com/","68.67.80.202:41271","socks");
		//"http://aruljohn.com/details.php"
		br.getBrowser().get("http://aruljohn.com/details.php");//"https://www.whatismybrowser.com/detect/is-flash-installed");
		Object result = ((PhantomJSDriver) br.getBrowser()).executeScript("console.warn('should log return');"+"return navigator.mimeTypes['application/x-shockwave-flash'];");
		Object result2 = ((PhantomJSDriver) br.getBrowser()).executeScript("console.warn('should log return');"+"return navigator.javaEnabled();");
		Main.log.info(result);
		br.getBrowser().quit();
	}
	
	public Browser() {
		browser = getBrowser(null, BrowserVersion.random(), "p");
	}
	
	public void changeProxy(Proxy proxy) {
		if (proxy == null) {
			String js = "phantom.setProxy('',0);";
			((PhantomJSDriver) browser).executePhantomJS(js);
			return;
		}
		
		String ip = proxy.getIp(), port = "" + proxy.getPort();
		Proxy.Type type = proxy.getType();
		
		String js = "phantom.setProxy('" + ip + "'," + port + ",'" + (type == Proxy.Type.SOCKS ? "socks5" : "http") + "');";
		
		((PhantomJSDriver) browser).executePhantomJS(js);
	}
	
	public WebDriver getBrowser(String proxy, BrowserVersion version, String browser) {
		
		//java.util.logging.Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);
		//java.util.logging.Logger.getLogger(PhantomJSDriver.class.getName()).setLevel(Level.OFF);
		//java.util.logging.Logger.getLogger("global").setLevel(Level.WARNING);
		
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
		
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Referer", "https://www.facebook.com/" );
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept-Language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4");
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		
		
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] { "--webdriver-loglevel=INFO","--ignore-ssl-errors=true", "--ssl-protocol=any"});
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", version.ua());
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", false);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "javascriptEnabled", true);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "javascriptCanOpenWindows", false);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "javascriptCanCloseWindows", false);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "debug", true);
		
		capabilities.setCapability("marionette", false);
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
		
		//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().setSize(new Dimension(800,600));
		setupDriver(driver, version.pl());
		
		return driver;
	}
	
	private void setupDriver(WebDriver driver,Platform platform){
		String onInitialized =
				"var page = this;" +
				"page.onInitialized = function () { " +
					//"console.warn('onInitialized');" +
					"page.evaluate(function () {" +
						//"page.reload();"+
							"navigator = {\n" +
								"plugins: [" +
									"{" +
									"description : 'Shockwave Flash 23.0 r0'," +
									"filename : 'pepflashplayer.dll'," +
									"name : 'Shockwave Flash'" +
									"}" +
								"]," +
								"mimeTypes: [ " +
									"{" +
									"description: 'Shockwave Flash'," +
									"suffixes: 'swf'," +
									"type : 'application/x-shockwave-flash', " +
									"enabledPlugin: " +
										"{" +
										"description : 'Shockwave Flash 23.0 r0'," +
										"filename : 'pepflashplayer.dll'," +
										"name : 'Shockwave Flash'" +
										"}" +
									"}," +
									"{" +
									"description: 'Shockwave Flash'," +
									"suffixes: 'spl'," +
									"type: 'application/futuresplash', " +
									"enabledPlugin: " +
										"{" +
										"description : 'Shockwave Flash 23.0 r0'," +
										"filename : 'pepflashplayer.dll'," +
										"name : 'Shockwave Flash'" +
										"}" +
									"}" +
								"]," +
								"appCodeName: 'Mozilla'," +
								"appName: 'Netscape'," +
								"appVersion: '5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22',\n" +
								"cookieEnabled: true," +
								"language: 'en'," +
								"onLine: true," +
								"javaEnabled : function(){ return true;}," +
								"platform: '"+platform.name()+"'," +
								"product: 'Gecko'," +
								"productSub: '20030107'," +
								"userAgent: 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22',\n" +
							"};" +
							
					"});" +
				"};";
		
		String onLoadFinished =
				"var page = this;" +
					"page.onLoadFinished = function () {" +
						"page.stop();" +
						//"console.warn('loadfinish');" +
						"page.evaluate(function(){" +
						"window.stop();" +
					"});" +
				"};";
		
		String onUrlChanged =
				"var page = this;" +
				"page.onUrlChanged = function() {" +
					"page.stop();" +
				"};";
		
		String onRequest =
				"var page = this;" +
				"page.onResourceRequested = function(requestData, networkRequest){" +
				
					" var skip = [" +
						"'googleads.g.doubleclick.net'," +
						"'cm.g.doubleclick.net'," +
						"'www.googleadservices.com'," +
						"'http://www.google-analytics.com/analytics.js'," +
						"'https://apis.google.com/js/plusone.js'" +
					"];" +
					
					"skip.forEach(function(needle) {" +
					"    if (requestData.url.indexOf(needle) > 0) {" +
					"      networkRequest.cancel();" +
					"    }" +
				"};";
		
		String onConfirm =
				"var page = this;" +
				"this.onConfirm = function(msg) {" +
						//"page.evaluate(function(){" +
						//"console.warn('CONFIRM: ' + msg);" +
						"return true;" +
						//"});" +
				"};";
		
		String onAlert =
				"var page = this;" +
				"page.onAlert = function(msg) {" +
						//"page.evaluate(function(){" +
						//"console.warn('ALERT: ' + msg);" +
						"return true;" +
						//"});" +
						"};";
		
		String onPrompt =
				"var page = this;" +
				"page.onPrompt = function(msg) {" +
						//"page.evaluate(function(){" +
						//"console.warn('PROMPT: ' + msg);" +
						"return true;" +
						//"});" +
						"};";
		
		PhantomJSDriver phantom = (PhantomJSDriver) driver;
		
		phantom.executePhantomJS(onInitialized);
		phantom.executePhantomJS(onLoadFinished);
		phantom.executePhantomJS(onAlert);
		phantom.executePhantomJS(onConfirm);
		phantom.executePhantomJS(onPrompt);
		phantom.executePhantomJS(onRequest);
		//phantom.executePhantomJS(onUrlChanged);
	}
	
	public WebDriver getBrowser() {
		return browser;
	}
}
