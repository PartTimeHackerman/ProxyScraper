package org.scraper.comp.scrapers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.scraper.comp.Globals;
import org.scraper.comp.Main;
import org.scraper.comp.checker.ProxyChecker;
import org.scraper.comp.web.Browser;

import java.io.IOException;
import java.util.List;

public class CssScraper implements Scraper{
	@Override
	public List<String> scrape(String url) throws IOException, InterruptedException {
		return scrape(url, null);
	}
	
	@Override
	public List<String> scrape(String url, ProxyChecker checker) throws InterruptedException {
		Main.log.info("CSS scraping {}", url);
		
		Browser browser = Globals.getBrowsersQueue().take();
		WebDriver driver = browser.getBrowser();
		
		driver.get(url);
		String copyableBeforeAfter = "var all = document.getElementsByTagName(\"*\");\n" +
				"for(var i=0;i<all.length;i++){\n" +
				"\tvar e = all.item(i);\n" +
				"if(e.tagName==\"BODY\") continue;\n" +
				"\ttry{\n" +
				"\t\tvar before = getComputedStyle(e,\":before\").content.replace(/\\\"/g,\"\");\n" +
				"\t\tvar after = getComputedStyle(e,\":after\").content.replace(/\\\"/g,\"\");\n" +
				"\t\t\tif(before != \"\" || after != \"\") e.innerHTML = before + e.innerHTML + after;\n" +
				"\t}catch(ex){}\n" +
				"}";
		((JavascriptExecutor) driver).executeScript(copyableBeforeAfter);
		
		String text = driver.findElement(By.tagName("body")).getText();
		Globals.getBrowsersQueue().put(browser);
		
		return checker!=null ? RegexMatcher.match(text,checker) : RegexMatcher.match(text);
	}
}
