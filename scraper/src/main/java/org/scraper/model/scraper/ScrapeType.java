package org.scraper.model.scraper;

import com.google.gson.annotations.SerializedName;

public enum ScrapeType {
	
	@SerializedName("0")
	NORMAL,
	@SerializedName("1")
	CSS,
	@SerializedName("2")
	OCR,
	
	
	// Do not add new here \/
	@SerializedName("3")
	UNCHECKED,
	@SerializedName("4")
	BLACK,
}
