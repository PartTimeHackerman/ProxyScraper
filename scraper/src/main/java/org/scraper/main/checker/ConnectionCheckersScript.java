package org.scraper.main.checker;

import org.scraper.main.Proxy;

public class ConnectionCheckersScript implements IConnectionCheckers {
	
	private ConnectionCheck httpsCheck;
	
	public ConnectionCheckersScript(){
		httpsCheck = new ConnectionCheckScript(Proxy.Type.HTTPS);
		ConnectionCheck httpCheck = new ConnectionCheckScript(Proxy.Type.HTTP);
		ConnectionCheck socksCheck = new ConnectionCheckScript(Proxy.Type.SOCKS);
		
		httpsCheck.setSuccessor(httpCheck);
		httpCheck.setSuccessor(socksCheck);
	}
	
	@Override
	public ConnectionCheck check(Proxy proxy, Integer timeout) {
		return httpsCheck.connect(proxy, timeout);
	}
}
