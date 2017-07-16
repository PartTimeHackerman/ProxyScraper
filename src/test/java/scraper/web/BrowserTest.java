package scraper.web;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import static org.junit.Assert.assertNotNull;

public class BrowserTest {
	
	private static Browser browser;
	
	@BeforeClass
	public static void setUp() throws Exception {
		browser = new Browser();
		assertNotNull(browser.getDriver());
	}
	
	@AfterClass
	public static void shutdown() throws Exception {
		browser.shutdown();
	}
	
	@Test
	public void getPID() throws Exception {
		Long pid = browser.getPID((PhantomJSDriver) browser.getDriver());
		assertNotNull(pid);
	}
	
}