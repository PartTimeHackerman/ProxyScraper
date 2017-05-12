package scraper.checker;

import scraper.Proxy;

public abstract class ConnectionHandler {
	
	protected ConnectionHandler successor;
	
	public void setSuccessor(ConnectionHandler successor) {
		this.successor = successor;
	}
	
	public ConnectionCheck handleConnection(Proxy proxy, Integer timeout) {
		if (successor != null)
			return successor.handleConnection(proxy, timeout);
		return null;
	}
}
