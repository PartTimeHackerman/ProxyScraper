package org.scraper.model.managers;

import org.scraper.model.scrapers.ocr.OCR;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

public class OcrQueue extends Queue<OCR> {
	
	public OcrQueue(Integer size){
		queue = new ArrayBlockingQueue<>(size);
		create();
	}
	
	@Override
	void create() {
		IntStream.range(0, getMaxSize())
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
