package org.scraper.model.modles;

import org.scraper.model.MainPool;
import org.scraper.model.managers.ProxyManager;

import java.util.concurrent.atomic.AtomicBoolean;

public class BarModel {
	
	private ProxyManager proxyManager;
	
	private AtomicBoolean checkOnFly;
	
	public BarModel(ProxyManager proxyManager, AtomicBoolean checkOnFly){
		this.proxyManager = proxyManager;
		this.checkOnFly = checkOnFly;
	}
	
	public Integer getThreads(){
		return MainPool.getInstance().getThreads();
	}
	
	public void setThreads(Integer threads){
		MainPool.getInstance().setThreads(threads);
	}
	
	public Integer getProxiesLimit(){
		return proxyManager.getLimit();
	}
	
	public void setProxiesLimit(Integer limit){
		proxyManager.setLimit(limit);
	}
	
	public void setCheckOnFly(Boolean check){
		checkOnFly.set(check);
	}
}
