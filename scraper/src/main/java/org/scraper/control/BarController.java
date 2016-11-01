package org.scraper.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;
import org.scraper.model.Interval;
import org.scraper.model.Pool;
import org.scraper.model.modles.MainModel;

public class BarController {
	@FXML
	private TextField threadsField;
	
	@FXML
	private TextField proxiesField;
	
	@FXML
	private ToggleButton checkOnFly;
	
	@FXML
	private Label threadsUsed;
	
	@FXML
	private Label threadsMax;
	
	@FXML
	private Label browsersUsed;
	
	@FXML
	private Label browsersMax;
	
	@FXML
	private Label ocrUsed;
	
	@FXML
	private Label ocrMax;
	
	private MainModel model;
	
	
	@FXML
	public void initialize(Pool pool) {
		this.model = model;
		
		threadsField.setText("" + model.pool().getThreads());
		threadsField.setOnAction(e ->
										 model.pool().setThreads(Integer.parseInt(threadsField.getText()))
								);
		threadsField.textProperty().addListener( (observable, oldValue, newValue) -> {
			if(!newValue.matches("[0-9]*"))
				threadsField.setText(oldValue);
		});
		
		proxiesField.setText("" + model.getProxyManager().getLimit());
		proxiesField.setOnAction(e ->
										 model.getProxyManager().setLimit(Integer.parseInt(proxiesField.getText()))
								);
		proxiesField.textProperty().addListener( (observable, oldValue, newValue) -> {
			if(!newValue.matches("[0-9]*"))
				proxiesField.setText(oldValue);
		});
		
		checkOnFly.setOnAction(e -> model.setCheckOnFly(checkOnFly.isSelected()));
		
		Interval.addAction("threads", var -> Platform.runLater(() -> threadsUsed.setText(var.toString())));
		Interval.addAction("threadsMax", var -> Platform.runLater(() -> threadsMax.setText(var.toString())));
		
		Interval.addAction("browsers", var -> Platform.runLater(() -> browsersUsed.setText(var.toString())));
		Interval.addAction("browsersMax", var -> Platform.runLater(() -> browsersMax.setText(var.toString())));
		
		Interval.addAction("ocrs", var -> Platform.runLater(() -> ocrUsed.setText(var.toString())));
		Interval.addAction("ocrsMax", var -> Platform.runLater(() -> ocrMax.setText(var.toString())));
		
	}
	
}
