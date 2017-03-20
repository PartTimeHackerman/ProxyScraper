package org.scraper.main.checker;

import org.scraper.main.Proxy;

public class ProxyInfo {
	
	private long time;
	
	private Proxy.Type type;
	
	private Proxy.Anonymity anonymity;
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public Proxy.Type getType() {
		return type;
	}
	
	public void setType(Proxy.Type type) {
		this.type = type;
	}
	
	public Proxy.Anonymity getAnonymity() {
		return anonymity;
	}
	
	public void setAnonymity(Proxy.Anonymity anonymity) {
		this.anonymity = anonymity;
	}
}
