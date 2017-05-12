package org.scraper.view.control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.scraper.view.model.GeneralOptions;
import org.scraper.main.Interval;

public class BarController {
	@FXML
	private TextField threadsField;
	
	/*@FXML
	private TextField proxiesField;*/
	
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
	public void initialize(GeneralOptions generalOptions) {
		
		threadsField.setText("" + generalOptions.getThreads());
		threadsField.setOnAction(e ->
										 generalOptions.setThreads(Integer.parseInt(threadsField.getText()))
								);
		threadsField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(!newValue.matches("[0-9]*"))
				threadsField.setText(oldValue);
		});
		
		/*proxiesField.setText("" + barModel.getProxiesLimit());
		proxiesField.setOnAction(e ->
										 barModel.setProxiesLimit(Integer.parseInt(proxiesField.getText()))
								);
		proxiesField.textProperty().addListener( (observable, oldValue, newValue) -> {
			if(!newValue.matches("[0-9]*"))
				proxiesField.setText(oldValue);
		});*/
		
		checkOnFly.setOnAction(e ->  generalOptions.setCheckOnFly(checkOnFly.isSelected()));
		
		Interval.addAction("threads", var -> Platform.runLater(() -> threadsUsed.setText(var.toString())));
		Interval.addAction("threadsMax", var -> Platform.runLater(() -> threadsMax.setText(var.toString())));
		
		Interval.addAction("browsers", var -> Platform.runLater(() -> browsersUsed.setText(var.toString())));
		Interval.addAction("browsersMax", var -> Platform.runLater(() -> browsersMax.setText(var.toString())));
		
		Interval.addAction("ocrs", var -> Platform.runLater(() -> ocrUsed.setText(var.toString())));
		Interval.addAction("ocrsMax", var -> Platform.runLater(() -> ocrMax.setText(var.toString())));
		
	}
	
}
