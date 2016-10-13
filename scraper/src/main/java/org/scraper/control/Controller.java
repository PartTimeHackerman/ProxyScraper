package org.scraper.control;

import javafx.scene.control.TextArea;
import org.scraper.model.Proxy;
import org.scraper.model.scrapers.ScrapeType;

public interface Controller {
	
	void scrape();
	
	void check();
	
	void crawl();
	
	void setCheckOnFly(boolean checkOnFly);
	
	void setGatherDepth(int depth);
	
	void filterByType(ScrapeType type);
	
	void filterByAnonymity(Proxy.Anonymity anonymity);
	
	void filterByTimeout(double timeout);
	
	void setTextArea(TextArea area);
	
	void updateCheck(String text, TextArea nonEditable);
	
	
}
