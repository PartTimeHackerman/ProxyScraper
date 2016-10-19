package org.scraper.control;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.scraper.model.Proxy;
import org.scraper.model.managers.ProxyTableManager;

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
	private TableColumn<Proxy, String> timeoutColumn;
	
	@FXML
	private Label proxies;
	
	@FXML
	private Label checked;
	
	@FXML
	private Label working;
	
	
	private Integer lastSize = 1;
	
	private IntegerProperty actualSize = new SimpleIntegerProperty();
	
	
	@FXML
	public void initialize() {
		
		setColumns();
		
		table.getColumns().addListener(this::onChange);
		
		ListProperty<Proxy> prop = new SimpleListProperty<>(ProxyTableManager.getVisible());
		//prop.addListener(this::changed);
		
		proxies.textProperty().bind(prop.sizeProperty().asString());
		
		//actualSize.bind(prop.sizeProperty());
		
	}
	
	private void changed(ObservableValue<? extends ObservableList<CharSequence>> observable, ObservableList<CharSequence> oldValue, ObservableList<CharSequence> newValue) {
		//lastSize.set(oldValue.size());
	}
	
	private void onChange(ListChangeListener.Change change) {
		if (actualSize.get() > lastSize) {
			lastSize = actualSize.get();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setColumns() {
		
		proxyColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getText()));
		
		typeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParamOne()));
		
		anonymityColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParamTwo()));
		
		timeoutColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParamThree()));
		
		setBrokenUncheckedStyle(typeColumn);
		
		table.setItems(ProxyTableManager.getVisible());
	}
	
	private void setBrokenUncheckedStyle(TableColumn<Proxy, String> column) {
		column.setCellFactory(new Callback<TableColumn<Proxy, String>, TableCell<Proxy, String>>() {
			@Override
			public TableCell<Proxy, String> call(TableColumn<Proxy, String> param) {
				return new TableCell<Proxy, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!empty && (item.equals("BROKEN") || item.equals("UNCHECKED"))) {
							this.getTableRow().setStyle("-fx-background-color: rgb(255, 231, 239)");
							this.setStyle("-fx-border-style:hidden hidden hidden hidden");
						} else {
							this.getTableRow().setStyle("");
							this.setStyle("");
						}
						setText(item);
						//sort(timeoutColumn, TableColumn.SortType.DESCENDING);
					}
				};
			}
		});
	}
	
	private void sort(TableColumn column, TableColumn.SortType sortType){
		column.setSortType(sortType);
		table.getSortOrder().add(column);
	}
}
