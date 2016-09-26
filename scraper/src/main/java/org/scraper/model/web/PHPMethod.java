package org.scraper.model.web;

public enum PHPMethod {

	POST_SITES("post_sites"),
	GET_SITES("get_sites"),
	
	POST_DOMAINS("post_domains"),
	GET_DOMAINS("get_domains"),
	
	POST_CLICKS("post_clicks"),
	GET_CLICKS("get_clicks"),
	

	GET_LINKS("get_links");

	private String cmd;

	PHPMethod(String cmd){
		this.cmd=cmd;
	}

	public String cmd(){
		return cmd;
	}
}
