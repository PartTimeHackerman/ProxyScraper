package scraper.web;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class BrowserTest {
	
	private static Browser browser;
	
	@BeforeClass
	public static void setUp() throws Exception {
		browser = new Browser();
    }
	
	@Test
	public void changeProxy() throws Exception {
		
	}
	
	@Test
	public void getDriver() throws Exception {
		
	}
	
	@Test
	public void getPID() throws Exception {
		Long pid = browser.getPID((PhantomJSDriver) browser.getDriver());
		
		assertNotNull(pid);
	}
	
	@AfterClass
	public static void shutdown() throws Exception {
		browser.shutdown();
	}
	
}