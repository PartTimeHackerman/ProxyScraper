package org.scraper.model.modles;

import org.scraper.model.Pool;
import org.scraper.model.managers.ProxyManager;

import java.util.concurrent.atomic.AtomicBoolean;

public class BarModel {
	
	private Pool pool;
	
	private ProxyManager proxyManager;
	
	private AtomicBoolean checkOnFly;
	
	public BarModel(Pool pool, ProxyManager proxyManager, AtomicBoolean checkOnFly){
		this.pool = pool;
		this.proxyManager = proxyManager;
		this.checkOnFly = checkOnFly;
	}
	
	public Integer getThreads(){
		return pool.getThreads();
	}
	
	public void setThreads(Integer threads){
		pool.setThreads(threads);
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
