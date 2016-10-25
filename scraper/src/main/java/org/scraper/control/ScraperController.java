package org.scraper.control;

import javafx.scene.control.TextArea;
import org.scraper.model.Proxy;
import org.scraper.model.modles.InterfaceModel;
import org.scraper.model.modles.MainModel;
import org.scraper.model.scrapers.ScrapeType;

public class ScraperController implements Controller {
	
	private InterfaceModel model;
	
	public ScraperController(String[] args){
		//model = new MainModel(); //TODO set args
	}
	
	
	@Override
	public void scrape() {
		model.scrape();
	}
	
	@Override
	public void check() {
		model.scrape();
	}
	
	@Override
	public void crawl() {
		model.crawl();
	}
	
	@Override
	public void setCheckOnFly(boolean checkOnFly) {
		model.setCheckOnFly(checkOnFly);
	}
	
	@Override
	public void setGatherDepth(int depth) {
		model.setGatherDepth(depth);
	}
	
	@Override
	public void filterByType(ScrapeType type) {
		//TODO
	}
	
	@Override
	public void filterByAnonymity(Proxy.Anonymity anonymity) {
		//TODO
	}
	
	@Override
	public void filterByTimeout(double timeout) {
		//TODO
	}
	
	@Override
	public void setTextArea(TextArea area) {
	}
	
	@Override
	public void updateCheck(String text, TextArea nonEditable) {
	}
}
