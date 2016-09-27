package org.scraper.model;

import java.util.List;

public interface InterfaceModel extends Model {
	
	void setCheckOnFly(boolean checkOnFly);
	void switchModel();
	void save();
	void load();
	void addToText(List<String> text);
}
