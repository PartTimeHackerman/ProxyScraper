package org.scraper.main.checker;

import org.scraper.main.Proxy;

public class ConnectionCheckersExternal implements IConnectionCheckers {
	
	private ConnectionCheck httpsCheck;
	
	public ConnectionCheckersExternal(){
		httpsCheck = new ConnectionCheckExternal(Proxy.Type.HTTPS);
		ConnectionCheck httpCheck = new ConnectionCheckExternal(Proxy.Type.HTTP);
		ConnectionCheck socksCheck = new ConnectionCheckExternal(Proxy.Type.SOCKS);
		
		httpsCheck.setSuccessor(httpCheck);
		httpCheck.setSuccessor(socksCheck);
	}
	
	@Override
	public ConnectionCheck check(Proxy proxy, Integer timeout) {
		return httpsCheck.connect(proxy, timeout);
	}
}
