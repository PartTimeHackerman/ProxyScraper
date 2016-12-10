package org.scraper.main.manager;

import org.scraper.main.web.Browser;
import org.scraper.main.web.BrowserConcurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

public class BrowserQueue extends Queue<Browser> {
	
	public BrowserQueue(Integer size){
		queue = new ArrayBlockingQueue<>(size);
		create();
	}
	
	@Override
	public Browser take() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			return new BrowserConcurrent();
		}
	}
	
	@Override
	void create() {
		IntStream.range(0, getMaxSize())
				.forEach(i -> {
					try {
						queue.put(new BrowserConcurrent());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
	}
	
	@Override
	public void shutdownAll() {
		queue.forEach(Browser::shutdown);
	}
}
