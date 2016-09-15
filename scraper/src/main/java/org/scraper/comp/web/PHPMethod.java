package org.scraper.comp.web;

public enum PHPMethod {

	POST_ADDRS("post_addrs"),
	GET_ADDRS("get_addrs"),

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
