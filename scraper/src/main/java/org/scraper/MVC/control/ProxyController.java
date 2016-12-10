package org.scraper.MVC.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.scraper.main.Proxy;
import org.scraper.MVC.model.ProxyModel;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProxyController implements ISaveable, ILoader {
	
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
	
	private ISelectable<Proxy> selectable;
	
	
	@FXML
	public void initialize(ProxyModel model, ProxyTableController selectable) {
		this.model = model;
		this.selectable = selectable;
		
		setupInput();
	}
	
	public void check() {
		model.check(selectable.getSelected());
	}
	
	private void setupInput() {
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
	
	private void setupTypeFilter() {
		Arrays.stream(Proxy.Type.values())
				.forEach(type ->
								 typeFilter.getItems().add(type));
		typeFilter.setValue(typeFilter.getItems().get(0));
		typeFilter.setOnAction(event ->
									   model.filterType(typeFilter.getSelectionModel().getSelectedItem()));
	}
	
	private void setupAnonymityFilter() {
		Arrays.stream(Proxy.Anonymity.values())
				.forEach(anonymity ->
								 anonymityFilter.getItems().add(anonymity));
		anonymityFilter.setValue(anonymityFilter.getItems().get(0));
		anonymityFilter.setOnAction(event ->
											model.filterAnonymity(anonymityFilter.getSelectionModel().getSelectedItem()));
	}
	
	private void setupTimeoutFilter() {
		filterTimeoutField.setOnAction(event ->
											   model.filterTimeout(Double.valueOf(filterTimeoutField.getText()).floatValue() * 1000));
	}
	
	
	@Override
	public void save(File file) {
		List<String> selectedAsStrings = selectable.getSelected().stream()
				.map(Proxy::getIpPort)
				.collect(Collectors.toList());
		
		saveFile(selectedAsStrings, file);
		
	}
	
	@Override
	public void load(File file) {
		List<String> loaded = readFile(file);
		
		loaded.forEach(string ->
							   model.addProxy(string));
		
	}
}
