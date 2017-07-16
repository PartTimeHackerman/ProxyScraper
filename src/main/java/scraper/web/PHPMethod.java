package scraper.web;

public enum PHPMethod {
	
	POST_SITES("post_sites"),
	GET_SITES("get_sites"),
	
	POST_DOMAINS("post_domains"),
	GET_DOMAINS("get_domains");
	
	private String cmd;
	
	PHPMethod(String cmd) {
		this.cmd = cmd;
	}
	
	public String cmd() {
		return cmd;
	}
}
