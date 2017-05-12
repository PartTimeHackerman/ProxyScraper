package scraper.scraper;

import scraper.Proxy;
import scraper.data.Site;

import java.util.ArrayList;
import java.util.List;

public abstract class ScraperAbstract implements Comparable<ScraperAbstract> {
	
	protected ScrapeType type;
	
	protected List<Proxy> proxy = new ArrayList<>();
	
	protected IProxyMatcher matcher = new ByNextMatcher();
	
	public abstract List<Proxy> scrape(Site site);
	
	public List<Proxy> getScraped() {
		return proxy;
	}
	
	public ScrapeType getType() {
		return type;
	}
	
	private Integer getScrapedNum() {
		return proxy.size();
	}
	@Override
	
	public int compareTo(ScraperAbstract o){
		int compare = getScrapedNum() - o.getScrapedNum();
		if(compare==0 && getType().ordinal() > o.getType().ordinal()) return -1;
		if(compare==0 && getType().ordinal() < o.getType().ordinal()) return 1;
		return compare;
	}
	
	public void setMatcher(IProxyMatcher matcher) {
		this.matcher = matcher;
	}
}
