package org.scraper.comp;

import java.util.Observable;
import java.util.Observer;

public class GlobalObserver implements Observer {
	
	private Globals globals;
	
	private static GlobalObserver observer = null;
	
	public GlobalObserver(Globals globals){
		this.globals=globals;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof Proxy) {
			Proxy proxy = (Proxy) arg;
			globals.addProxy(proxy);
			globals.getLinksManager().clickAll(proxy);
		}
	}
	
	private GlobalObserver() {
	}
	
	public static GlobalObserver getObserver() {
		if (observer == null)
			observer = new GlobalObserver();
		return observer;
	}
}
