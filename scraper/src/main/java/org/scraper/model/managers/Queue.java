package org.scraper.model.managers;

import org.scraper.model.MainLogger;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class Queue<T> {
	
	BlockingQueue<T> queue;
	
	public synchronized void setSize(Integer size) {
		BlockingQueue<T> newQueue = new ArrayBlockingQueue<>(size);
		
		queue.forEach(browser -> {
			try {
				newQueue.put(browser);
			} catch (InterruptedException ignored) {}
		});
		
		queue.drainTo(new ArrayList<>(), 0);
		queue = newQueue;
	}
	
	public Integer getSize() {
		return queue.size();
	}
	
	public Integer getMaxSize() {
		return queue.size() + queue.remainingCapacity();
	}
	
	public Integer remainingCapacity() {
		return queue.remainingCapacity();
	}
	
	public T take() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			MainLogger.log().warn("Can't take object from queue");
			return null;
		}
	}
	
	public void put(T o) {
		try {
			queue.offer(o, 1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			MainLogger.log().warn("Can't put object in queue");
		}
	}
	
	abstract void create();
	
	abstract void shutdownAll();
}
