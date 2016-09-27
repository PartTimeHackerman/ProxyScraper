package org.scraper.model;

import org.scraper.model.checker.Connection;

public class Proxy implements Text{
	
	private String ip;
	
	private int port;
	
	private String ipPort;
	
	private Type type;
	
	private Anonymity anonymity;
	
	private long speed;
	
	private boolean checked;
	
	private boolean working;
	
	public Proxy(String ip, int port) {
		setIp(ip);
		setPort(port);
		setIpPort(ip + ":" + port);
		setChecked(false);
		
	}
	
	public Proxy(String ipPort) {
		this(ipPort.split(":")[0], Integer.parseInt(ipPort.split(":")[1]));
	}
	
	public Proxy setProxy(Connection connection){
		setType(connection.getType());
		setAnonymity(connection.getAnonymity());
		setSpeed(connection.getTime());
		setChecked(true);
		working = true;
		return this;
	}
	
	
	@Override
	public String toString(){
		return getIpPort() + " " + getType() + " " + getAnonymity() + " " + getSpeed();
	}
	
	@Override
	public boolean equals(Object o){
		return ((Proxy) o).getIpPort().equals(getIpPort());
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getPort() {
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
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Anonymity getAnonymity() {
		return anonymity;
	}
	
	public void setAnonymity(Anonymity anonymity) {
		this.anonymity = anonymity;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(long speed) {
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
	
	@Override
	public String getText() {
		return ipPort;
	}
	
	
	public enum Type {
		HTTP,
		HTTPS,
		SOCKS
	}
	
	public enum Anonymity {
		TRANSPARENT,
		ANONYMOUS,
		ELITE
	}
}
