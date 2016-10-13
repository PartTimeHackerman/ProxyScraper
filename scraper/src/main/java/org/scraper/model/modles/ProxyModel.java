package org.scraper.model.modles;

import org.scraper.model.Main;

public class ProxyModel implements Model {
	
	private MainModel mainModel;
	
	public ProxyModel(MainModel mainModel){
		this.mainModel = mainModel;
	}
	
	@Override
	public void scrape() {
		Main.log.warn("Can't scrape ip addresses!");
	}
	
	@Override
	public void check() {
	}
	
	@Override
	public void crawl() {
		Main.log.warn("Can't crawl ip addresses!");
	}
	
}
