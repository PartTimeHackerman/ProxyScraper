package scraper.checker;

import scraper.Proxy;

public class ConnectionCheckScript extends ConnectionCheck {
	
	private static final String URL = "http://lifechangertoworse.ga/ping.php";
	private static final String TITLE = "Letters";
	
	public ConnectionCheckScript(Proxy.Type type) {
		super(type, URL, TITLE);
	}
	
	public Proxy.Anonymity getAnonymity() {
		String text = response.text();
		String anonymity = "";
		if (text.contains("|"))
			anonymity = text.split("\\|")[1];
		switch (anonymity) {
			case "e": {
				return Proxy.Anonymity.ELITE;
			}
			case "a": {
				return Proxy.Anonymity.ANONYMOUS;
			}
			case "t": {
				return Proxy.Anonymity.TRANSPARENT;
			}
			default: {
				return Proxy.Anonymity.TRANSPARENT;
			}
		}
	}
	
	
}
