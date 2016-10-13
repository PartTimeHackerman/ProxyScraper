package org.scraper.control;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import org.scraper.model.Proxy;
import org.scraper.model.managers.ProxyTableManager;
import org.scraper.model.managers.SitesManager;
import org.scraper.model.web.Site;

public class SiteTableController {
	
	@FXML
	private HBox textBox;
	
	@FXML
	private TableView<Site> table;
	
	@FXML
	private TableColumn<Site, String> siteColumn;
	
	@FXML
	private TableColumn<Site, String> proxiesColumn;
	
	@FXML
	private TableColumn<Site, String> workingColumn;
	
	@FXML
	private Label sites;
	
	private SitesManager manager;
	
	@FXML
	public void initialize(SitesManager manager) {
		this.manager = manager;
		
		setColumns();
		
		
		ListProperty<Proxy> prop = new SimpleListProperty<>(ProxyTableManager.getVisible());
		//prop.addListener(this::changed);
		
		sites.textProperty().bind(prop.sizeProperty().asString());
		
		//actualSize.bind(prop.sizeProperty());
		
	}
	
	private void changed(ObservableValue<? extends ObservableList<CharSequence>> observable, ObservableList<CharSequence> oldValue, ObservableList<CharSequence> newValue) {
		//lastSize.set(oldValue.size());
	}
	
	
	@SuppressWarnings("unchecked")
	private void setColumns() {
		
		siteColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getText()));
		
		proxiesColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParamOne()));
		
		workingColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getParamTwo()));
		
		table.setItems(manager.getVisible());
	}
	
}
