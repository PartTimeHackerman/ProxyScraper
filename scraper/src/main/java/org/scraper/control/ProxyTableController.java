package org.scraper.control;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.scraper.model.MainLogger;
import org.scraper.model.Proxy;
import org.scraper.model.modles.ProxyModel;

import java.util.List;

public class ProxyTableController implements ISelectable<Proxy> {
	
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
	
	private ObservableList<Proxy> all;
	
	private ObservableList<Proxy> selected;
	
	
	@FXML
	public void initialize(ProxyModel model) {
		setColumns();
		
		table.setItems(model.getVisible());
		
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		proxies.textProperty().bind(model.getAllSize().asString());
		working.textProperty().bind(model.getWorkingSize().asString());
		
		selected = table.getSelectionModel().getSelectedItems();
		all = table.getItems();
		
		table.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.DELETE)
				model.deleteSelected(getSelected());
		});
		
		table.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.C)
				model.handleCopy(selected);
		});
		
		table.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.isControlDown() && event.getCode() == KeyCode.V)
				model.handlePaste();
		});
		
		table.getItems().addListener(new ListChangeListener<Proxy>() {
			@Override
			public void onChanged(Change<? extends Proxy> c) {
				table.refresh();
				MainLogger.log().info("table changed");
			}
		});
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
		Double speed = (double) data.getValue().getSpeed() / 1000;
		return speed > 0 ? round(speed, 1) : null;
	}
	
	
	private Double round(Number number, Integer decimalPlaces) {
		Integer places = (int) Math.pow(10, decimalPlaces);
		return (double) Math.round(number.doubleValue() * places) / places;
	}
	
	@Override
	public List<Proxy> getSelected() {
		return selected.size() > 0 ? getList(selected) : getList(all);
	}
	
	private <E> List<E> getList(ObservableList<E> observableList) {
		return observableList.subList(0, observableList.size());
	}
}
