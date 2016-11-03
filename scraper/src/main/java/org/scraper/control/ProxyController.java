package org.scraper.control;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.scraper.model.Proxy;
import org.scraper.model.modles.ProxyModel;

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
	private Button check;
	
	private ProxyModel model;
	
	private TableView<Proxy> table;
	
	@FXML
	public void initialize(ProxyModel model, TableView<Proxy> table) {
		this.model = model;
		this.table = table;
		
		setupInput();
	}
	
	public void check() {
		ObservableList<Proxy> selected = table.getSelectionModel().getSelectedItems();
		
		if (selected.size() > 0) {
			model.check(selected.subList(0, selected.size()));
		} else {
			ObservableList<Proxy> all = table.getItems();
			model.check(all.subList(0, all.size()));
		}
	}
	
	private void setupInput(){
		setupFilters();
		
		showBroken.setOnAction(event ->
									   model.showBroken(showBroken.isSelected()));
		
		check.setOnAction(event ->
								  check());
	}
	
	private void setupFilters() {
		setupTypeFilter();
		setupAnonymityFilter();
		setupTimeoutFilter();
	}
	
	private void setupTypeFilter(){
		Arrays.stream(Proxy.Type.values())
				.forEach(type ->
								 typeFilter.getItems().add(type));
		typeFilter.setValue(typeFilter.getItems().get(0));
		typeFilter.setOnAction(event ->
									   model.filterType(typeFilter.getSelectionModel().getSelectedItem()));
	}
	
	private void setupAnonymityFilter(){
		Arrays.stream(Proxy.Anonymity.values())
				.forEach(anonymity ->
								 anonymityFilter.getItems().add(anonymity));
		anonymityFilter.setValue(anonymityFilter.getItems().get(0));
		anonymityFilter.setOnAction(event ->
											model.filterAnonymity(anonymityFilter.getSelectionModel().getSelectedItem()));
	}
	
	private void setupTimeoutFilter(){
		filterTimeoutField.setOnAction(event ->
											  model.filterTimeout(Double.valueOf(filterTimeoutField.getText()).floatValue() * 1000));
	}
}
