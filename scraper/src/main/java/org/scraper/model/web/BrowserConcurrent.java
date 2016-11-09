package org.scraper.model.web;

import org.scraper.model.IConcurrent;

public class BrowserConcurrent extends Browser implements IConcurrent {
	
	public BrowserConcurrent(){
		send(this::setUp);
	}
	
	@Override
	public void shutdown() {
		send(super::shutdown);
	}
	
}
