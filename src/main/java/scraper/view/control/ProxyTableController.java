package scraper.view.control;

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
import scraper.view.model.ProxyModelFX;
import scraper.Proxy;

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
	
	//@FXML
	//private Label working;
	
	private ObservableList<Proxy> all;
	
	private ObservableList<Proxy> selected;
	
	
	@FXML
	public void initialize(ProxyModelFX model) {
		setColumns();
		
		table.setItems(model.getVisible());
		
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		proxies.textProperty().bind(model.getAllSize().asString());
		//working.textProperty().bind(main.getWorkingSize().asString());
		
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
		
		table.getItems().addListener((ListChangeListener<Proxy>) c -> {
			table.refresh();
			//MainLogger.log().info("table changed");
		});
		
		String help =
		"Upper menu bar:\n" +
		"Threads input - set number of avaiable threads\n" +
		"Check on fly button - assgn proxies right after scrape\n" +
		"Threads field - shows avaiable threads\n" +
		"Browsers field - shows avaiable browsers (needed for most sites)\n" +
		"OCRs field - shows avaiable ocrs (needed for sites that have proxies saved in images)\n" +
		"\n" +
		"Proxy tab:\n" +
		"Table - You can sort proxies by eg. Timeout by clicking on corresponging column,\n" +
		"filter by Type, Anonimity and Timout and save and load proxies from .txt files through left menu.\n" +
		"Show broken - show or hide broken proxies. \n" +
		"Check Proxies - assgn ALL proxies in table.       \n" +
		"To assgn, copy (CTRL+C), or delete specyfic proxy select it ( or select several by holding CTRL an selecting).\n" +
		"\n" +
		"Sites tab:\n" +
		"Most of options are self explanatory and similar to Proxy tab.\n" +
		"Scrape - scrape and don't save any scraping stats. (Scraped proxies will be in Proxy tab)\n" +
		"Check Sites - scrape and save scraping stats like avg. sites and avg. working.\n" +
		"Gather - checks all links of site,\n if linked site has proxies saves it and do the gather to self etc. until max depth is reached.\n"+
		"\n"+
		"Some tips:\n"+
		"Don't use too many threads, it can freeze Your computer\n"+
		"ALWAYS close Scraper through X in upper left corner due to running browsers (it needs to be repaired)\n"+
		"Refresh tables by sorting them if they won't autorefresh\n";
		
		Label label = new Label();
		label.setText(help);
		table.setPlaceholder(label);
		
		
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
