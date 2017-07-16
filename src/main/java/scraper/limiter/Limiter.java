package scraper.limiter;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Limiter {
	
	private Integer limit;
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	private Vector<Switchable> switchables = new Vector<>();
	
	public Limiter(Integer limit) {
		setLimit(limit);
	}
	
	public void addSwitchable(Switchable switchable) {
		switchables.add(switchable);
	}
	
	public void removeSwitchable(Switchable switchable) {
		switchables.remove(switchable);
	}
	
	public void incrementBy(Integer value) {
		counter.addAndGet(value);
		if (counter.get() >= limit)
			turnOffSwitchables();
	}
	
	public void clear() {
		counter.set(0);
		turnOnSwitchables();
	}
	
	private void turnOffSwitchables() {
		switchables.forEach(switchable -> {
			if (switchable.isOn())
				switchable.turnOff();
		});
	}
	
	private void turnOnSwitchables() {
		switchables.forEach(switchable -> {
			if (!switchable.isOn())
				switchable.turnOn();
		});
	}
	
	public Integer getLimit() {
		return limit;
	}
	
	public void setLimit(Integer limit) {
		if (limit > 0)
			this.limit = limit;
		else if (limit == 0)
			this.limit = Integer.MAX_VALUE;
		else
			this.limit = 0;
	}
}
