package org.scraper.control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.scraper.model.Proxy;
import org.scraper.model.managers.Checkable;
import org.scraper.model.managers.ProxyTableManager;

import java.util.Arrays;

public class ProxyController {
	
	@FXML
	private ComboBox<Proxy.Type> typeFilter;
	@FXML
	private ComboBox<Proxy.Anonymity> anonymityFilter;
	@FXML
	private TextField filterTimeoutField;
	@FXML
	private ToggleButton showBroken;
	
	@FXML
	public void initialize() {
		setupFields();
	}
	
	private void setupFields() {
		Arrays.stream(Proxy.Type.values())
				.forEach(type ->
								 typeFilter.getItems().add(type));
		typeFilter.setValue(typeFilter.getItems().get(0));
		typeFilter.setOnAction(event ->
									   ProxyTableManager.filterType(typeFilter.getSelectionModel().getSelectedItem()));
		
		Arrays.stream(Proxy.Anonymity.values())
				.forEach(anonymity ->
								 anonymityFilter.getItems().add(anonymity));
		anonymityFilter.setValue(anonymityFilter.getItems().get(0));
		anonymityFilter.setOnAction(event ->
											ProxyTableManager.filterAnonymity(anonymityFilter.getSelectionModel().getSelectedItem()));
		
		filterTimeoutField.setOnAction(event ->
											   ProxyTableManager.filterTimeout(Double.valueOf(filterTimeoutField.getText()).floatValue() * 1000));
		
		showBroken.setOnAction(event ->
									   ProxyTableManager.showBroken(showBroken.isSelected()));
		
	}
	
	public enum SortBy {
		TIMEOUT,
		TYPE,
		ANONYMITY,
	}
	
}
