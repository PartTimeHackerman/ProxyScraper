package org.scraper.main.manager;

import org.scraper.main.MainLogger;
import org.scraper.main.web.Browser;
import org.scraper.main.web.BrowserConcurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

public class BrowserQueue extends Queue<Browser> {
	
	public BrowserQueue(Integer size) {
		queue = new ArrayBlockingQueue<>(size);
		create();
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
	public void create() {
		IntStream.range(0, getMaxSize())
				.forEach(i -> putToQueue());
	}
	
	private void putToQueue() {
		new Thread(() ->
				   {
					   try {
						   queue.put(new Browser());
					   } catch (InterruptedException e) {
						   MainLogger.log().error("Thread was interrupted");
					   }
				   }).start();
	}
	
	@Override
	public void shutdown() {
		queue.forEach(browser -> {
			queue.clear();
			new Thread(browser::shutdown).start();
		});
	}
}
