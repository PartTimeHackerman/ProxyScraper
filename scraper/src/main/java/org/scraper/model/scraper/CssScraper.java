package org.scraper.model.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.scraper.model.Main;
import org.scraper.model.Proxy;
import org.scraper.model.web.Site;
import org.scraper.model.web.Browser;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CssScraper extends Scraper{
	
	private BlockingQueue<Browser> browsers;
	
	public CssScraper(BlockingQueue<Browser> browsers){
		type = ScrapeType.CSS;
		this.browsers = browsers;
	}
	
	@Override
	public List<Proxy> scrape(Site site) throws InterruptedException {
		String url = site.getAddress();
		Main.log.info("CSS scraping {}", url);
		
		Browser browser = browsers.take();
		WebDriver driver = browser.getBrowser();
		
		driver.get(url);
		driver.findElement(By.tagName("body"));
		String copyableBeforeAfter =
				"var all = document.getElementsByTagName(\"*\");" +
				"for(var i=0;i<all.length;i++){" +
					"var e = all.item(i);" +
					"if(e.tagName=='body') continue;" +
					"try{" +
						"var before = getComputedStyle(e,\":before\").content.replace(/\\\"/g,\"\");" +
						"var after = getComputedStyle(e,\":after\").content.replace(/\\\"/g,\"\");" +
						"if(before != \"\" || after != \"\") e.innerHTML = before + e.innerHTML + after;" +
					"}catch(ex){}" +
				"}" +
						
				"[].forEach.call(document.getElementsByTagName(\"br\"), function (e) { e.innerHTML += \"||newline||\";});" +
				"[].forEach.call(document.getElementsByTagName(\"p\"), function (e) { e.innerHTML += \"||newline||\";});" +
				"[].forEach.call(document.getElementsByTagName(\"tr\"), function (e) { e.innerHTML += \"||newline||\";});";
		((JavascriptExecutor) driver).executeScript(copyableBeforeAfter);
		WebElement body = driver.findElement(By.tagName("body"));
		
		String text = body.getText();
		browsers.put(browser);
		
		proxy = RegexMatcher.match(text);
		return proxy;
	}
	
	
}
