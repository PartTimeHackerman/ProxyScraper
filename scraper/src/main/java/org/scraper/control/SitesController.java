package org.scraper.control;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.scraper.model.managers.SitesManager;
import org.scraper.model.modles.SitesModel;
import org.scraper.model.web.Site;

import java.util.Arrays;
import java.util.List;

public class SitesController {
	@FXML
	private Button scrapeButton;
	
	@FXML
	private Button checkButton;
	
	@FXML
	private TextField addField;
	
	@FXML
	private Button addSite;
	
	@FXML
	private Button gatherButton;
	
	@FXML
	private TextField depthField;
	
	private SitesModel model;
	
	private TableView<Site> table;
	
	private ObservableList<Site> all;
	
	private ObservableList<Site> selected;
	
	private SitesManager manager;
	
	@FXML
	public void initialize(SitesModel model, SitesManager sitesManager, TableView<Site> table) {
		
		this.model = model;
		this.manager = sitesManager;
		
		this.table = table;
		this.selected = table.getSelectionModel().getSelectedItems();
		this.all = table.getItems();
		
		scrapeButton.setOnAction(event -> scrape());
		
		checkButton.setOnAction(event -> check());
		
		gatherButton.setOnAction(event -> gather());
		
		addSite.setOnAction(event -> addSites());
		
		depthField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("[0-9]*"))
				depthField.setText(oldValue);
		});
	}
	
	public void addSites() {
		String sitesString = addField.getText();
		addField.clear();
		
		String[] sitesArr = sitesString.contains(" ") ? sitesString.split(" ") :
				sitesString.contains("\n") ? sitesString.split("\n") : new String[]{};
		
		Arrays.stream(sitesArr).forEach(siteString -> manager.addSite(siteString));
	}
	
	private void scrape() {
		model.scrape(getSelected());
	}
	
	private void check() {
		model.check(getSelected());
	}
	
	private void gather() {
		model.gather(getSelected(), Integer.parseInt(depthField.getText()));
	}
	
	private List<Site> getSelected() {
		return selected.size() > 0 ? getList(selected) : getList(all);
	}
	
	private <E> List<E> getList(ObservableList<E> observableList) {
		return observableList.subList(0, observableList.size());
	}
	
}
