package scraper.web;

import scraper.IConcurrent;

public class BrowserConcurrent extends Browser implements IConcurrent {
	
	public BrowserConcurrent(){
		//Explicitly call a constructor from a super class
		super();
		
		Runnable runnable = BrowserConcurrent.super::setUp;
		new Thread(runnable).start();
	}
	
	@Override
	public void shutdown() {
		Runnable runnable = BrowserConcurrent.super::shutdown;
		new Thread(runnable).start();
		
		//send(super::shutdown);
	}
}
