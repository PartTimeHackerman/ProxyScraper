package org.scraper.MVC.model;

import org.scraper.main.Pool;
import org.scraper.main.data.ProxyRepo;
import org.scraper.main.limiter.Limiter;

import java.util.concurrent.atomic.AtomicBoolean;

public class GeneralOptions {
	
	private ProxyRepo proxyRepo;
	
	private AtomicBoolean checkOnFly;
	
	private Limiter limiter;
	
	private Pool pool;
	
	public GeneralOptions(ProxyRepo proxyRepo, AtomicBoolean checkOnFly, Limiter limiter, Pool pool){
		this.proxyRepo = proxyRepo;
		this.checkOnFly = checkOnFly;
		this.limiter = limiter;
		this.pool = pool;
	}
	
	public Integer getThreads(){
		return pool.getThreads();
	}
	
	public void setThreads(Integer threads){
		pool.setThreads(threads);
	}
	
	public Integer getProxiesLimit(){
		return limiter.getLimit();
	}
	
	public void setProxiesLimit(Integer limit){
		limiter.setLimit(limit);
	}
	
	public void setCheckOnFly(Boolean check){
		checkOnFly.set(check);
	}
}
