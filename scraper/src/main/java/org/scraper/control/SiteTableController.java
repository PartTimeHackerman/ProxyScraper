package org.scraper.control;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
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
	
}
