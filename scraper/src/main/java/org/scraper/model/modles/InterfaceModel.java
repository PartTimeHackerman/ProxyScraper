package org.scraper.model.modles;

import java.util.List;

public interface InterfaceModel extends Model {
	
	void setCheckOnFly(boolean checkOnFly);
	void setGatherDepth(Integer depth);
	void switchModel();
	void save();
	void load();
}
