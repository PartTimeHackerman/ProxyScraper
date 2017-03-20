package org.scraper.main.manager;

import org.scraper.main.scraper.ocr.OCR;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.IntStream;

public class OcrQueue extends Queue<OCR> {
	
	public OcrQueue(Integer size){
		queue = new ArrayBlockingQueue<>(size);
		create();
	}
	
	@Override
	public void create() {
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
	void shutdown() {
		queue.forEach(OCR::shutdown);
	}
}
