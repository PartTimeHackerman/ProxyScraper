package org.scraper.model.managers;

import org.scraper.model.web.Browser;
import org.scraper.model.web.BrowserConcurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class BrowserQueue extends Queue<Browser> {
	
	public BrowserQueue(Integer size){
		queue = new ArrayBlockingQueue<>(size);
		//create();
	}
	
	@Override
	public Browser take() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			return new Browser();
		}
	}
	
	@Override
	void create() {
		IntStream.range(0, getSize()+1)
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
