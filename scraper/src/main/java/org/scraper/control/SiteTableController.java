package org.scraper.control;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import org.scraper.model.managers.SitesManager;
import org.scraper.model.modles.SitesModel;
import org.scraper.model.web.Site;

public class SiteTableController {
	
	@FXML
	private HBox textBox;
	
	@FXML
	private TableView<Site> table;
	
	@FXML
	private TableColumn<Site, String> siteColumn;
	
	@FXML
	private TableColumn<Site, Number> proxiesColumn;
	
	@FXML
	private TableColumn<Site, Number> workingColumn;
	
	@FXML
	private TableColumn<Site, String> typeColumn;
	
	@FXML
	private Label sites;
	
	private SitesModel model;
	
	@FXML
	public void initialize(SitesModel model) {
		this.model = model;
		
		setColumns();
		
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		ListProperty<Site> prop = new SimpleListProperty<>(table.getItems());
		
		sites.textProperty().bind(prop.sizeProperty().asString());
		
		
	}
	
	private void changed(ObservableValue<? extends ObservableList<CharSequence>> observable, ObservableList<CharSequence> oldValue, ObservableList<CharSequence> newValue) {
		//lastSize.set(oldValue.size());
	}
	
	
	@SuppressWarnings("unchecked")
	private void setColumns() {
		
		siteColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getText()));
		
		proxiesColumn.setCellValueFactory(cellData -> cellData.getValue().getAvgSites() != -1 ? new ReadOnlyIntegerWrapper(cellData.getValue().getAvgSites()) : null);
		
		workingColumn.setCellValueFactory(cellData -> cellData.getValue().getAvgWorking() != -1 ? new ReadOnlyIntegerWrapper(cellData.getValue().getAvgWorking()) : null);
		
		typeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getType().name()));
		
		table.setItems(model.getVisible());
	}
	
	public TableView<Site> getTable() {
		return table;
	}
}
