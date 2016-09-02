package org.scraper.comp.scraper;

public enum PostGetPHP {

	POST_ADDRS("post_addrs"),
	GET_ADDRS("get_addrs"),

	POST_CLICKS("post_clicks"),
	GET_CLICKS("get_clicks"),

	GET_LINKS("get_links");

	private String cmd;

	PostGetPHP(String cmd){
		this.cmd=cmd;
	}

	public String cmd(){
		return cmd;
	}
}
