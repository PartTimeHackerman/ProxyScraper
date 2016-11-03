package org.scraper.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.application.Platform;
import org.scraper.model.Interval;
import org.scraper.model.modles.BarModel;

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
	
	
	@FXML
	public void initialize(BarModel barModel) {
		
		threadsField.setText("" + barModel.getThreads());
		threadsField.setOnAction(e ->
										 barModel.setThreads(Integer.parseInt(threadsField.getText()))
								);
		threadsField.textProperty().addListener( (observable, oldValue, newValue) -> {
			if(!newValue.matches("[0-9]*"))
				threadsField.setText(oldValue);
		});
		
		proxiesField.setText("" + barModel.getProxiesLimit());
		proxiesField.setOnAction(e ->
										 barModel.setProxiesLimit(Integer.parseInt(proxiesField.getText()))
								);
		proxiesField.textProperty().addListener( (observable, oldValue, newValue) -> {
			if(!newValue.matches("[0-9]*"))
				proxiesField.setText(oldValue);
		});
		
		checkOnFly.setOnAction(e ->  barModel.setCheckOnFly(checkOnFly.isSelected()));
		
		Interval.addAction("threads", var -> Platform.runLater(() -> threadsUsed.setText(var.toString())));
		Interval.addAction("threadsMax", var -> Platform.runLater(() -> threadsMax.setText(var.toString())));
		
		Interval.addAction("browsers", var -> Platform.runLater(() -> browsersUsed.setText(var.toString())));
		Interval.addAction("browsersMax", var -> Platform.runLater(() -> browsersMax.setText(var.toString())));
		
		Interval.addAction("ocrs", var -> Platform.runLater(() -> ocrUsed.setText(var.toString())));
		Interval.addAction("ocrsMax", var -> Platform.runLater(() -> ocrMax.setText(var.toString())));
		
	}
	
}
