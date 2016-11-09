package org.scraper.model.managers;

import org.scraper.model.modles.MainModel;
import org.scraper.model.scrapers.OCR;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

public class OcrQueue extends Queue<OCR> {
	
	public OcrQueue(Integer size){
		queue = new ArrayBlockingQueue<>(size);
		create();
	}
	
	@Override
	void create() {
		IntStream.range(0, getSize()+1)
				.forEach(i -> {
					try {
						queue.put(new OCR());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
	}
	
	@Override
	void shutdownAll() {
		queue.forEach(OCR::shutdown);
	}
}
