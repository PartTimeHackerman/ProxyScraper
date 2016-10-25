package org.scraper.control;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.scraper.model.Proxy;
import org.scraper.model.checker.ProxyChecker;
import org.scraper.model.managers.ProxyManager;
import org.scraper.model.managers.ProxyTableModel;
import org.scraper.model.modles.ProxyModel;
import org.scraper.model.scrapers.RegexMatcher;

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
	
	
	private Proxy.Type visibleType = Proxy.Type.ALL;
	private Proxy.Anonymity visibleAnonymity = Proxy.Anonymity.ALL;
	
	private TableView<Proxy> table;
	
	@FXML
	public void initialize(ProxyModel model, TableView<Proxy> table) {
		this.model = model;
		this.table = table;
		setupFields();
	}
	
	private void setupFields() {
		Arrays.stream(Proxy.Type.values())
				.forEach(type ->
								 typeFilter.getItems().add(type));
		typeFilter.setValue(typeFilter.getItems().get(0));
		typeFilter.setOnAction(event ->
									   model.filterType(typeFilter.getSelectionModel().getSelectedItem()));
		
		Arrays.stream(Proxy.Anonymity.values())
				.forEach(anonymity ->
								 anonymityFilter.getItems().add(anonymity));
		anonymityFilter.setValue(anonymityFilter.getItems().get(0));
		anonymityFilter.setOnAction(event ->
											model.filterAnonymity(anonymityFilter.getSelectionModel().getSelectedItem()));
		
		filterTimeoutField.setOnAction(event ->
											   model.filterTimeout(Double.valueOf(filterTimeoutField.getText()).floatValue() * 1000));
		
		showBroken.setOnAction(event ->
									  model.showBroken(showBroken.isSelected()));
		
		check.setOnAction(event -> check());
		
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
	
	
	public enum SortBy {
		TIMEOUT,
		TYPE,
		ANONYMITY,
	}
	
}
