package org.scraper.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.scraper.model.modles.MainModel;

public class BarController {
	@FXML
	private TextField threadsField;
	
	@FXML
	private TextField threadsField1;
	
	@FXML
	private ToggleButton checkOnFly;
	
	private MainModel model;
	
	@FXML
	public void initialize(MainModel model) {
		
		this.model = model;
	}
	
}
