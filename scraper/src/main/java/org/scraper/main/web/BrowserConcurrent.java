package org.scraper.main.web;

import org.scraper.main.IConcurrent;

public class BrowserConcurrent extends Browser implements IConcurrent {
	
	public BrowserConcurrent(){
		//Explicitly call a constructor from a super class
		super(null);
		
		send(super::setUp);
	}
	
	@Override
	public void shutdown() {
		send(super::shutdown);
	}
}
