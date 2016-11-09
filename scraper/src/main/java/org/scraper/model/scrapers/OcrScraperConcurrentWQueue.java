package org.scraper.model.scrapers;

import org.jsoup.Connection;
import org.opencv.core.Mat;
import org.scraper.model.managers.QueuesManager;
import org.scraper.model.modles.MainModel;
import org.scraper.model.web.ConcurrentConnectionExecutor;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OcrScraperConcurrentWQueue extends OcrScraper {
	
	@Override
	protected List<Connection.Response> getResponses(List<Connection> connections) {
		return ConcurrentConnectionExecutor.getInstance().execute(connections);
	}
	
	@Override
	protected String doOcr(Mat mat){
		OCR ocr = QueuesManager.getInstance()
				.getOcrQueue()
				.take();
		
		String imgText = ocr.read(mat);
		
		QueuesManager.getInstance()
				.getOcrQueue()
				.put(ocr);
		return imgText;
	}
}
