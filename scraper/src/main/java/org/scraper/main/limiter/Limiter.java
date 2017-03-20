package org.scraper.main.limiter;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Limiter {
	
	private Integer limit;
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	private Vector<Switchable> switchables = new Vector<>();
	
	public Limiter(Integer limit) {
		this.limit = limit;
	}
	
	public void addSwitchable(Switchable switchable) {
		switchables.add(switchable);
	}
	
	public void removeSwitchable(Switchable switchable) {
		switchables.remove(switchable);
	}
	
	public void incrementBy(Integer value) {
		counter.addAndGet(value);
		if(counter.get() >= limit)
			turnOffSwitchables();
	}
	
	public void clear(){
		counter.set(0);
		turnOnSwitchables();
	}
	
	public void setLimit(Integer limit){
		this.limit = limit;
	}
	
	private void turnOffSwitchables() {
		switchables.forEach(switchable -> {
			if (switchable.isOn())
				switchable.turnOff();
		});
	}
	
	private void turnOnSwitchables(){
		switchables.forEach(switchable -> {
			if (!switchable.isOn())
				switchable.turnOn();
		});
	}
	
	public Integer getLimit() {
		return limit;
	}
}
