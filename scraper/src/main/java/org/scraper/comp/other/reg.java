package org.scraper.comp.other;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;

public class reg {

	private static Map<Integer, Person> personMap;

	static {
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
	}

	public static void main(String[] args) {
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\geckodriver.exe");
		Persons persons = new Persons();
		personMap = persons.getPersons();

		Map<Integer, Person> newPersonMap = new HashMap<>();
		Maker maker = new Maker();


		int c = 1;
		ExecutorService executor = Executors.newFixedThreadPool(c);
		CountDownLatch latch = new CountDownLatch(c);
		for (int j = 0; j < c; j++) {
			executor.submit(()->{
				WebDriver driver = maker.getDriver();
				System.out.println("Startin");
				for (int i = 0; i < 100; i++) {
					Person person = new Person();
					if (maker.register(person, driver)) {
						String pass = maker.getPass(person, driver);
						if (pass.length() > 0) {
							if (maker.login(person, driver, pass)) {
								maker.giveFan(driver);
							} else {
								System.out.println("Log err");
							}
						} else {
							System.out.println("Pass err");
							driver.close();
							driver = maker.getDriver();
						}
					} else {
						System.out.println("Reg err");
					}
				}
				driver.close();
				driver.quit();
				latch.countDown();
			});
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		System.exit(0);
	}

}

class Maker {

	public WebDriver getDriver() {


		String USER_AGENT = "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0";

		/*String proxy = getPrx();
		Proxy proxy1 = new Proxy();
		proxy1.setHttpProxy(proxy)
				.setSslProxy(proxy)
				.setFtpProxy(proxy);*/

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("marionette", true);
		//capabilities.setCapability(CapabilityType.PROXY, proxy1);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", USER_AGENT);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "loadImages", false);
		capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "javascriptEnabled", true);
		capabilities.setPlatform(Platform.WIN8_1);
		capabilities.setBrowserName("Mozilla Firefox");
		capabilities.setVersion("47");


		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-agent=" + USER_AGENT);

		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		final WebDriver client = new ChromeDriver(capabilities);

		client.manage().timeouts().implicitlyWait(10, TimeUnit.MINUTES);

		client.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL , "t");
		return client;
	}

	private String getPrx(){
		System.out.println("Gettin prx");
		String prx = "58.247.125.205:10037\n" +
				"218.75.10.171:3128\n" +
				"186.67.158.43:3128\n" +
				"183.61.236.54:3128\n" +
				"120.52.72.23:80\n" +
				"52.221.231.240:3128\n" +
				"103.56.219.117:3128\n" +
				"110.230.101.208:8888\n" +
				"119.88.128.73:80\n" +
				"220.175.166.171:8888\n" +
				"58.247.125.205:9401\n" +
				"134.196.214.127:3128\n" +
				"47.90.38.239:3128\n" +
				"113.253.63.48:80\n" +
				"60.191.160.20:3128\n" +
				"182.253.16.114:8080\n" +
				"186.95.74.129:8080\n" +
				"182.107.2.102:9529\n" +
				"115.29.34.2:3128\n" +
				"58.247.125.205:10220\n" +
				"87.116.201.201:8080";

		String[] addArr = prx.split("\n");
		Integer childsNum = addArr.length;
		Integer randNum = (int) (Math.random() * childsNum);
		String[] proxy = addArr[randNum].split(":");
		Integer port = Integer.parseInt(proxy[1]);

		try {
			Connection.Response res = Jsoup.connect("http://www.wp.pl/").userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
					.proxy(proxy[0], port).timeout(3000).execute();
		} catch (Exception e) {
			return getPrx();
		}
		return addArr[randNum];
	}

	public boolean register(Person person, WebDriver driver) {
		try {
			driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(0));
			String url = "http://www.fakemailgenerator.com/#/" + person.getEmail().split("@")[1] + "/" + person.getEmail().split("@")[0] + "/";
			driver.get(url);
			Thread.sleep(1000);

			driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(1));

			driver.get("http://www.wertungsheft.de/");
			driver.findElement(By.xpath("//*[@id=\"page-content\"]/header/ul[1]/li[3]/a")).click();

			driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div[2]/div[1]/input")).sendKeys(person.getEmail());
			driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div[2]/div[2]/div[1]/input")).sendKeys(person.getName().split(" ")[0]);
			driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div[2]/div[2]/div[2]/input")).sendKeys(person.getName().split(" ")[1]);
			driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div[2]/div[3]/input")).sendKeys(person.getName().split(" ")[0] + (int) (Math.random() * 100));
			driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div[3]/div[2]/div[4]/div/label/input")).click();

			WebElement day = driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div[2]/div[5]/div[1]/select"));
			day.click();
			for (int i = 0; i < Math.random() * 31; i++) {
				day.sendKeys(Keys.ARROW_DOWN);
			}
			WebElement month = driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div[2]/div[5]/div[2]/select"));
			month.click();
			for (int i = 0; i < Math.random() * 12; i++) {
				month.sendKeys(Keys.ARROW_DOWN);
			}

			WebElement year = driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div[2]/div[5]/div[3]/select"));
			year.click();
			for (int i = 0; i < 18 + (Math.random() * 40); i++) {
				year.sendKeys(Keys.ARROW_DOWN);
			}
			year.sendKeys(Keys.ENTER);

			driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div[2]/div[6]/button[2]")).click();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getPass(Person person, WebDriver driver) {
		try {
			String url = "http://www.fakemailgenerator.com/#/" + person.getEmail().split("@")[1] + "/" + person.getEmail().split("@")[0].toLowerCase() + "/";
			driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(0));
			driver.get(driver.findElement(By.id("emailFrame")).getAttribute("src"));

			WebElement pass = driver.findElement(By.cssSelector("body > div.content"));

			String passwort = pass.getText().split("Passwort: ")[1].split("Bitte ")[0].replace("\n", "");
			driver.switchTo().window(new ArrayList<>(driver.getWindowHandles()).get(1));
			return passwort;
		} catch (Exception e) {
			return "";
		}
	}

	public boolean login(Person person, WebDriver driver, String pass) {
		try {
			driver.get("http://www.wertungsheft.de/");
			driver.findElement(By.xpath("//*[@id=\"page-content\"]/header/ul[1]/li[2]/a")).click();

			driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div/div[1]/input")).sendKeys(person.getEmail());
			driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div/div[2]/input")).sendKeys(pass);
			driver.findElement(By.xpath("/html/body/div[3]/div/div/form/div[2]/div/div[4]/button[2]")).click();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean giveFan(WebDriver driver) {
		try {
			driver.get("http://www.wertungsheft.de/aktive/9249/celina-federschmidt/");
			driver.findElement(By.xpath("//*[@id=\"page-content\"]/main/div[1]/div[1]/div[2]/div/button")).click();

			driver.manage().deleteAllCookies();
			//driver.findElement(By.cssSelector("#page-content > header > ul.list-inline.right-nav.pull-right.hidden-xs > li:nth-child(2) > div > a")).click();
			//driver.findElement(By.xpath("//*[@id=\"page-content\"]/header/ul[1]/li[2]/div/ul/li[8]/a")).click();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

}

