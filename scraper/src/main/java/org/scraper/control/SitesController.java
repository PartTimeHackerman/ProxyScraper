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
	
	private SitesManager manager;
	
	@FXML
	public void initialize(SitesModel model, SitesManager sitesManager, TableView<Site> table) {
		
		this.model = model;
		this.table = table;
		this.manager = sitesManager;
		
		scrapeButton.setOnAction(event -> scrape());
		
		checkButton.setOnAction(event -> check());
		
		gatherButton.setOnAction(event -> crawl());
		
		addSite.setOnAction( event -> addSites());
	}
	
	public void addSites(){
		String sitesString = addField.getText();
		String[] sitesArr = sitesString.contains(" ") ? sitesString.split(" ") :
							sitesString.contains("\n") ? sitesString.split("\n") : new String[]{};
		
		Arrays.stream(sitesArr).forEach(siteString -> manager.addSite(siteString));
		
	}
	
	//TODO encapsulate
	private void scrape(){
		ObservableList<Site> selected = table.getSelectionModel().getSelectedItems();
		
		if (selected.size() > 0) {
			model.scrape(selected.subList(0, selected.size()));
		} else {
			ObservableList<Site> all = table.getItems();
			model.scrape(all.subList(0, all.size()));
		}
	}
	
	private void check(){
		ObservableList<Site> selected = table.getSelectionModel().getSelectedItems();
		
		if (selected.size() > 0) {
			model.check(selected.subList(0, selected.size()));
		} else {
			ObservableList<Site> all = table.getItems();
			model.check(all.subList(0, all.size()));
		}
	}
	
	private void crawl(){
		ObservableList<Site> selected = table.getSelectionModel().getSelectedItems();
		
		if (selected.size() > 0) {
			model.crawl(selected.subList(0, selected.size()));
		} else {
			ObservableList<Site> all = table.getItems();
			model.crawl(all.subList(0, all.size()));
		}
	}
	
}
