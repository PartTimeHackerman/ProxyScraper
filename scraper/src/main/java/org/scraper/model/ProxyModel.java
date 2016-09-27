package org.scraper.model;

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
		mainModel.getChecker().checkProxies(mainModel.getTextManager().getProxy());
		//TODO retun to textarea or observe checker
	}
	
	@Override
	public void crawl() {
		Main.log.warn("Can't crawl ip addresses!");
	}
	
}
