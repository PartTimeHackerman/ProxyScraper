ProxyScraper
===
[![Build Status](https://api.travis-ci.org/PartTimeHackerman/ProxyScraper.svg?branch=no-PhantomJS)](https://travis-ci.org/PartTimeHackerman/ProxyScraper)
[![codebeat badge](https://codebeat.co/badges/dc40fe4c-9d36-4345-a74b-d65f08cf7a12)](https://codebeat.co/projects/github-com-parttimehackerman-proxyscraper-no-phantomjs)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0188904a8bf64f37b741204700553ccf)](https://www.codacy.com/app/kuriozalnie/ProxyScraper?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PartTimeHackerman/ProxyScraper&amp;utm_campaign=Badge_Grade)

ProxyScraper is fairly simple public proxy scraper that allows the following: 
- gathering sites that contain proxy, 
- scraping proxies from sites,
- checking if proxy is working, what anonimity it's providing and with what speed it's working.

Gathering new sites (from google):
```
SitesScraper sitesScraper = new SitesScraper();
sitesScraper.scrapeSites();
List<Site> gathered = sitesScraper.getSites();
```

Gathering sites (from provided sites):
```
Integer depth = 2;
Integer threads = 20;
List<Site> sitesList = new ArrayList<>(Collections.singleton(new Site("http://sample.xyz", ScrapeType.UNCHECKED)));
LinkGatherConcurrent linkGather = new LinkGatherConcurrent(depth, new Pool(threads, 0L));
List<Site> gathered = linkGather.gatherSites(sitesList);
```

Scraping sites:
````
Integer threads = 20;
Integer timeout = 10000; // in millis.
Integer limit = 0; // no limit
Integer check = true; // check proxy right after scraping
Integer browsers = 5; // no. PhantomJS browsers
Integer ocrs = 1; // no. tesseract ocrs

Scraper scraper = new Scraper(threads, timeout, limit, check, browsers, ocrs);
List<Site> sitesList = new ArrayList<>(Collections.singleton(new Site("http://sample.xyz", ScrapeType.UNCHECKED)));
List<Proxy> proxies = scraper.getProxyScraper().scrapeList(sitesList);
````

Checking proxies:
````
...
List<Proxy> proxyList = new ArrayList<>(Collections.singleton(new Proxy("192.168.1.1", 8080)));
Scraper scraper = new Scraper(threads, timeout, limit, check, browsers, ocrs);
scraper.getProxyChecker().checkProxies(proxyList);
````
Before running You must add phantomjs.exe to executable path or set path to it like ```Browser.setPhantomJsPath("C:/some/path/phantomjs.exe");``` 

You can also use [branch] which already has PhantomJS bundled.

There's also branch with [view made in JavaFx].

[branch]: <https://github.com/PartTimeHackerman/ProxyScraper/tree/with-PhantomJS>
[view made in JavaFx]: <https://github.com/PartTimeHackerman/ProxyScraper/tree/javaFx>