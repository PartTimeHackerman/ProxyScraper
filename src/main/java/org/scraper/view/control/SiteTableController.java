package org.scraper.view.control;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.scraper.view.model.SitesModelFX;
import scraper.data.Site;

import java.util.List;

public class SiteTableController implements ISelectable<Site>{
	
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
	
	private SitesModelFX model;
	
	private ObservableList<Site> all;
	
	private ObservableList<Site> selected;
	
	@FXML
	public void initialize(SitesModelFX model) {
		this.model = model;
		
		setColumns();
		
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		ListProperty<Site> prop = new SimpleListProperty<>(table.getItems());
		
		sites.textProperty().bind(prop.sizeProperty().asString());
		
		selected = table.getSelectionModel().getSelectedItems();
		all = table.getItems();
		
		table.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if(event.getCode() == KeyCode.DELETE)
				model.deleteSelected(getSelected());
		});
		
		table.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if(event.isControlDown() && event.getCode() == KeyCode.C)
				model.handleCopy(selected);
		});
		
		table.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if(event.isControlDown() && event.getCode() == KeyCode.V)
				model.handlePaste();
		});
	}
	
	@SuppressWarnings("unchecked")
	private void setColumns() {
		
		siteColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAddress()));
		
		proxiesColumn.setCellValueFactory(cellData -> cellData.getValue().isAvgSitesAssigned() ? new ReadOnlyIntegerWrapper(cellData.getValue().getAvgProxies()) : null);
		
		workingColumn.setCellValueFactory(cellData -> cellData.getValue().isAvgWorkingAssigned() ? new ReadOnlyIntegerWrapper(cellData.getValue().getAvgWorkingProxies()) : null);
		
		typeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getType().name()));
		
		table.setItems(model.getVisible());
	}
	
	@Override
	public List<Site> getSelected() {
		return selected.size() > 0 ? getList(selected) : getList(all);
	}
	
	private <E> List<E> getList(ObservableList<E> observableList) {
		return observableList.subList(0, observableList.size());
	}
}
