package org.scraper.model;

import org.scraper.model.checker.IConnection;

public class Proxy {
	
	private String ip;
	
	private Integer port;
	
	private String ipPort;
	
	private Type type;
	
	private Anonymity anonymity;
	
	private Long speed = 0l;
	
	private boolean checked;
	
	private boolean working;
	
	public Proxy(String ip, Integer port) {
		setIp(ip);
		setPort(port);
		setIpPort(ip + ":" + port);
		setChecked(false);
		
	}
	
	public Proxy(String ipPort) {
		this(ipPort.split(":")[0], Integer.parseInt(ipPort.split(":")[1]));
	}
	
	public Proxy setUpProxy(IConnection connection) {
		setType(connection.getType());
		setAnonymity(connection.getAnonymity());
		setSpeed(connection.getTime());
		setWorking(true);
		return this;
	}
	
	
	@Override
	public String toString() {
		return getIpPort() + " " + getType() + " " + getAnonymity() + " " + getSpeed();
	}
	
	@Override
	public boolean equals(Object o) {
		return ((Proxy) o).getIpPort().equals(getIpPort());
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getIpPort() {
		return ipPort;
	}
	
	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}
	
	public Type getType() {
		return type;
	}
	
	public String getTypeString() {
		return type != null ? type.name() : "";
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Anonymity getAnonymity() {
		return anonymity;
	}
	
	public String getAnonymityString() {
		return anonymity != null ? anonymity.name() : "";
	}
	
	public void setAnonymity(Anonymity anonymity) {
		this.anonymity = anonymity;
	}
	
	public Long getSpeed() {
		return speed;
	}
	
	public void setSpeed(Long speed) {
		this.speed = speed;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public boolean isWorking() {
		return working;
	}
	
	public void setWorking(boolean working) {
		this.working = working;
	}
	
	
	public enum Type {
		ALL, //TODO this is for org.scraper.view only, encapsulate it or somerhing
		HTTP,
		HTTPS,
		SOCKS
	}
	
	public enum Anonymity {
		ALL, //TODO as above
		TRANSPARENT,
		ANONYMOUS,
		ELITE
	}
}
