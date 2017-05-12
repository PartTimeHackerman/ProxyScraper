package org.scraper.main.web;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.scraper.main.MainLogger;
import org.scraper.main.Proxy;
import org.scraper.main.TempFileManager;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class Browser {
	
	private final static String PATH = TempFileManager.loadResource(Browser.class, "phantomjs.exe").getAbsolutePath();
	
	private WebDriver driver;
	
	public Browser() {
		setUp();
	}
	
	protected Browser(Object explicit) {
	}
	
	protected void setUp() {
		driver = getBrowser(null, BrowserVersion.random(), false);
	}
	
	public void changeProxy(Proxy proxy) {
		if (proxy == null) {
			String js = "phantom.setUpProxy('',0);";
			((PhantomJSDriver) driver).executePhantomJS(js);
			return;
		}
		
		String ip = proxy.getIp(), port = "" + proxy.getPort();
		Proxy.Type type = proxy.getType();
		
		String js = "phantom.setUpProxy('" + ip + "'," + port + ",'" + (type == Proxy.Type.SOCKS ? "socks5" : "http") + "');";
		
		((PhantomJSDriver) driver).executePhantomJS(js);
	}
	
	private WebDriver getBrowser(String proxy, BrowserVersion version, Boolean debug) {
		if (!debug) {
			java.util.logging.Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);
			java.util.logging.Logger.getLogger(PhantomJSDriver.class.getName()).setLevel(Level.OFF);
			java.util.logging.Logger.getLogger("global").setLevel(Level.WARNING);
			java.util.logging.Logger.getLogger("org.openqa.selenium.remote").setLevel(Level.OFF);
		}
		
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
		
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, PATH);
		
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[]{"--webdriver-loglevel=" + (debug ? "INFO" : "OFF"), "--ignore-ssl-errors=true", "--ssl-protocol=any"});
		
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Referer", "https://www.facebook.com/");
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept-Language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4");
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX + "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", version.ua());
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", false);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "javascriptEnabled", true);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "javascriptCanOpenWindows", false);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "javascriptCanCloseWindows", false);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "debug", debug ? "true" : "false");
		
		//capabilities.setCapability("marionette", false);
		capabilities.setPlatform(version.pl());
		capabilities.setBrowserName(version.nm());
		capabilities.setVersion(String.valueOf((int) (Math.random() * 48)));
		
		WebDriver driver = new PhantomJSDriver(capabilities);
		
		driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS);
		driver.manage().timeouts().setScriptTimeout(3000, TimeUnit.MILLISECONDS);
		driver.manage().window().setSize(new Dimension(800, 600));
		setupDriver(driver, version.pl());
		
		return driver;
	}
	
	private void setupDriver(WebDriver driver, Platform platform) {
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
						"platform: '" + platform.name() + "'," +
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
		
		String onError =
				"var page = this;" +
						"page.onError = function(msg, trace) {" +
						"var msgStack = ['PHANTOM ERROR: ' + msg];\n" +
						"  if (trace && trace.length) {\n" +
						"    msgStack.push('TRACE:');\n" +
						"    trace.forEach(function(t) {\n" +
						"      msgStack.push(' -> ' + (t.file || t.sourceURL) + ': ' + t.line + (t.function ? ' (in function ' + t.function +')' : ''));\n" +
						"    });\n" +
						"  }\n" +
						"  console.error(msgStack.join('\\n'));" +
						//"});" +
						"};";
		
		PhantomJSDriver phantom = (PhantomJSDriver) driver;
		
		phantom.executePhantomJS(onInitialized);
		phantom.executePhantomJS(onLoadFinished);
		phantom.executePhantomJS(onAlert);
		phantom.executePhantomJS(onConfirm);
		phantom.executePhantomJS(onPrompt);
		phantom.executePhantomJS(onRequest);
		phantom.executePhantomJS(onError);
		//phantom.executePhantomJS(onUrlChanged);
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	public Long getPID(PhantomJSDriver driver) {
		String returnPidScript =
				"var system = require('system');" +
						"var pid = system.pid;" +
						"return pid;";
		
		return (Long) driver.executePhantomJS(returnPidScript);
	}
	
	public void shutdown() {
		driver.close();
		driver.quit();
		
		try {
			Runtime.getRuntime().exec("taskkill /F /PID " + getPID((PhantomJSDriver) driver));
		} catch (Exception e) {
			MainLogger.log(this).fatal("Driver is already closed");
		}
	}
}

