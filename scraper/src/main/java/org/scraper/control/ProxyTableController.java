package org.scraper.control;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.scraper.model.Proxy;
import org.scraper.model.modles.ProxyModel;

public class ProxyTableController {
	
	@FXML
	private TableView<Proxy> table;
	
	@FXML
	private TableColumn<Proxy, String> proxyColumn;
	
	@FXML
	private TableColumn<Proxy, String> typeColumn;
	
	@FXML
	private TableColumn<Proxy, String> anonymityColumn;
	
	@FXML
	private TableColumn<Proxy, Number> timeoutColumn;
	
	@FXML
	private Label proxies;
	
	@FXML
	private Label working;
	
	
	@FXML
	public void initialize(ProxyModel model) {
		setColumns();
		
		table.setItems(model.getVisible());
		
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		proxies.textProperty().bind(model.getAllSize().asString());
		working.textProperty().bind(model.getWorkingSize().asString());
		
		
	}
	
	@SuppressWarnings("unchecked")
	private void setColumns() {
		
		proxyColumn.setCellValueFactory(cellData ->
												new ReadOnlyStringWrapper(cellData.getValue().getIpPort()));
		
		typeColumn.setCellValueFactory(cellData ->
											   new ReadOnlyStringWrapper(cellData.getValue().getTypeString()));
		
		anonymityColumn.setCellValueFactory(cellData ->
													new ReadOnlyStringWrapper(cellData.getValue().getAnonymityString()));
		
		timeoutColumn.setCellValueFactory(this::getWrapper);
		
	}
	
	public TableView<Proxy> getTable() {
		return table;
	}
	
	private ReadOnlyDoubleWrapper getWrapper(TableColumn.CellDataFeatures data) {
		Double speed = getSpeed(data);
		
		return speed != null ? new ReadOnlyDoubleWrapper(speed) : null;
	}
	
	private <V extends Proxy, T> Double getSpeed(TableColumn.CellDataFeatures<V, T> data) {
		Long speed = data.getValue().getSpeed();
		return speed > 0 ? round(speed / 1000, 2) : null;
	}
	
	
	private Double round(Number number, Integer decimalPlaces) {
		Integer places = (int) Math.pow(10, decimalPlaces);
		return (double) Math.round(number.doubleValue() * places) / places;
	}
}
