package org.scraper.model.scrapers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.scraper.model.Proxy;
import org.scraper.model.modles.MainModel;
import org.scraper.model.web.Browser;
import org.scraper.model.web.Site;

import java.util.List;

class CssScraper extends Scraper {
	
	protected Browser browser;
	
	{
		type = ScrapeType.CSS;
	}
	
	CssScraper(Browser browser) {
		this.browser = browser;
	}
	
	@Override
	public List<Proxy> scrape(Site site) {
		String url = site.getAddress();
		MainModel.log.info("CSS scraping {}", url);
		
		WebDriver driver = browser.getBrowser();
		
		driver.get(url);
		if (driver.findElements(By.tagName("body")).size() == 0)
			return proxy;
		
		String copyableBeforeAfter =
				"try{" +
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
						"[].forEach.call(document.getElementsByTagName(\"tr\"), function (e) { e.innerHTML += \"||newline||\";});" +
						"}catch(e){console.warn('CATCHING');}";
		((PhantomJSDriver) driver).executePhantomJS(copyableBeforeAfter);
		WebElement body;
		body = driver.findElement(By.tagName("body"));
		
		
		String text = body.getText();
		
		proxy = matcher.match(text);
		return proxy;
	}
	
	
}
