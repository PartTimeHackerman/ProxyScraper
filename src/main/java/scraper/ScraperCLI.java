package scraper;


import org.apache.commons.cli.*;
import scraper.data.Site;
import scraper.gather.LinkGatherConcurrent;
import scraper.scraper.ScrapeType;
import scraper.scraper.SitesScraper;

import java.util.ArrayList;
import java.util.List;

public class ScraperCLI {
	
	private Scraper scraper;
	
	private Options options = new Options();
	private CommandLineParser parser = new DefaultParser();
	
	private Integer threads = 50;
	private Integer timeout = 10000;
	private Integer limit = 0;
	private Boolean check = false;
	private Integer browsers = 5;
	private Integer ocrs = 5;
	
	private String[] gather = {};
	private String[] sites = {};
	private String[] proxies = {};
	private Mode mode = null;
	
	private Integer depth = 2;
	
	public static void main(String[] args) {
		ScraperCLI cli = new ScraperCLI(args);
	}
	
	public ScraperCLI(String[] args) {
		buildOptions();
		setUpOptions(args);
	}
	
	private void buildOptions() {
		Option help = Option.builder("h")
				.longOpt("help")
				.required(false)
				.desc("show help")
				.build();
		
		Option threads = Option.builder("t")
				.longOpt("threads")
				.required(false)
				.desc("set number of threads")
				.hasArg()
				.type(Number.class)
				.build();
		
		Option proxyTimeOut = Option.builder("to")
				.longOpt("timeout")
				.required(false)
				.desc("set proxy timeout")
				.hasArg()
				.type(Number.class)
				.build();
		
		Option proxyLimit = Option.builder("l")
				.longOpt("limit")
				.required(false)
				.desc("set proxies limit")
				.hasArg()
				.type(Number.class)
				.build();
		
		Option proxyCheck = Option.builder("ac")
				.longOpt("autocheck")
				.required(false)
				.desc("check scraped proxies after scraping")
				.build();
		
		Option browsers = Option.builder("b")
				.longOpt("browsers")
				.required(false)
				.desc("set number of browsers")
				.hasArg()
				.type(Number.class)
				.build();
		
		Option OCRs = Option.builder("o")
				.longOpt("ocrs")
				.required(false)
				.desc("set number of OCRs")
				.hasArg()
				.type(Number.class)
				.build();
		
		
		Option scrapeSites = Option.builder("s")
				.longOpt("sites")
				.argName("sites")
				.required(false)
				.desc("[separated by space] scrape sites (http://top.kek)")
				.hasArgs()
				.type(String.class)
				.build();
		
		Option checkProxies = Option.builder("c")
				.longOpt("check")
				.argName("proxies")
				.required(false)
				.desc("[separated by space] check proxies (ip:port)")
				.hasArgs()
				.type(String.class)
				.build();
		
		Option gatherSites = Option.builder("g")
				.longOpt("gather")
				.argName("sites")
				.required(false)
				.desc("gather sites [separated by space] (http://top.kek)")
				.type(String.class)
				.build();
		
		options
				.addOption(help)
				.addOption(threads)
				.addOption(proxyTimeOut)
				.addOption(proxyLimit)
				.addOption(proxyCheck)
				.addOption(browsers)
				.addOption(OCRs)
				.addOption(scrapeSites)
				.addOption(checkProxies)
				.addOption(gatherSites)
		;
	}
	
	private void setUpOptions(String[] args) {
		try {
			CommandLine cmd = parser.parse(options, args);
			
			if (cmd.hasOption("h"))
				printHelp();
			
			if (cmd.hasOption("t"))
				threads = (Integer) cmd.getParsedOptionValue("t");
			
			if (cmd.hasOption("to"))
				timeout = (Integer) cmd.getParsedOptionValue("to");
			
			if (cmd.hasOption("l"))
				threads = (Integer) cmd.getParsedOptionValue("l");
			
			if (cmd.hasOption("ac"))
				check = true;
			
			if (cmd.hasOption("b"))
				threads = (Integer) cmd.getParsedOptionValue("b");
			
			if (cmd.hasOption("o"))
				threads = (Integer) cmd.getParsedOptionValue("o");
			
			
			if (cmd.hasOption("g"))
				gather = cmd.getOptionValues("g");
			
			if (cmd.hasOption("s"))
				sites = cmd.getOptionValues("s");
			
			if (cmd.hasOption("c"))
				proxies = cmd.getOptionValues("c");
			
			
			if (cmd.hasOption("g"))
				gather(gather);
			else if(cmd.hasOption("s"))
				scrapeSites(sites);
			else if (cmd.hasOption("c"))
				checkProxies(proxies);
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			System.out.println("use \"--help\" option for details.");
		}
	}
	
	private void printHelp() {
		new HelpFormatter().printHelp("ProxyScraper [OPTIONS]", options);
	}
	
	private void gather(String[] sites) {
		if (sites == null)
			gatherNewSites();
		else
			gatherFromSites(sites);
		
	}
	
	private void gatherNewSites() {
		List<Site> sitesList = new ArrayList<>();
		SitesScraper sitesScraper = new SitesScraper(sitesList);
		sitesScraper.scrapeSites();
		sitesList.forEach(System.out::println);
	}
	
	private void gatherFromSites(String[] sites) {
		List<Site> sitesList = getSitesList(sites);
		LinkGatherConcurrent linkGather = new LinkGatherConcurrent(depth, new Pool(threads, 0L));
		List<Site> gathered = linkGather.gatherSites(sitesList);
		gathered.forEach(System.out::println);
	}
	
	private void scrapeSites(String[] sites) {
		Scraper scraper = new Scraper(threads, timeout, limit, check, browsers, ocrs);
		List<Site> sitesList = getSitesList(sites);
		List<Proxy> proxies = scraper.getProxyScraper().scrapeList(sitesList);
		proxies.forEach(System.out::println);
	}
	
	private void checkProxies(String[] proxies){
		List<Proxy> proxyList = new ArrayList<>();
		for (String proxy : proxies) {
			proxyList.add(new Proxy(proxy));
		}
		Scraper scraper = new Scraper(threads, timeout, limit, check, browsers, ocrs);
		scraper.getProxyChecker().checkProxies(proxyList);
		proxyList.forEach(System.out::println);
	}
	
	private List<Site> getSitesList(String[] sites){
		List<Site> sitesList = new ArrayList<>();
		for (String site : sites) {
			sitesList.add(new Site(site, ScrapeType.UNCHECKED));
		}
		return sitesList;
	}
	
	private enum Mode{
		GATHER,
		SCRAPER,
		CHECKER
	}
}
