package org.scraper.main;

import org.scraper.main.checker.ConnectionCheck;

public class Proxy {
	
	private String ip;
	
	private Integer port;
	
	private String ipPort;
	
	private Type type;
	
	private Anonymity anonymity;
	
	private Long speed = 0L;
	
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
	
	public void setUpProxy(ConnectionCheck connection) {
		if (connection != null) {
			setType(connection.getType());
			setAnonymity(connection.getAnonymity());
			setSpeed(connection.getTime());
			setWorking(true);
		}
	}
	
	
	@Override
	public String toString() {
		StringBuilder proxySting = new StringBuilder(getIpPort());
		proxySting.append(" Type: ").append(getTypeString());
		proxySting.append(" Anonymity: ").append(getAnonymity());
		proxySting.append(" Speed: ").append(getSpeed());
		return proxySting.toString();
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
		return type != null
				? type.name()
				: isChecked()
					? "BROKEN"
					: "UNKNOWN";
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Anonymity getAnonymity() {
		return anonymity;
	}
	
	public String getAnonymityString() {
		return anonymity != null
				? anonymity.name()
				: isChecked()
					? "BROKEN"
					: "UNKNOWN";
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
		ALL,
		HTTP,
		HTTPS,
		SOCKS
	}
	
	public enum Anonymity {
		ALL,
		TRANSPARENT,
		ANONYMOUS,
		ELITE
	}
}
