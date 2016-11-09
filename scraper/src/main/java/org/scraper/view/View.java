package org.scraper.view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.scraper.control.*;
import org.scraper.model.Interval;
import org.scraper.model.modles.BarModel;
import org.scraper.model.modles.MainModel;
import org.scraper.model.modles.ProxyModel;
import org.scraper.model.modles.SitesModel;

public class View extends Application {
	
	@FXML
	private Label logBar;
	
	//private static Clipboard clipboard = Clipboard.getSystemClipboard();
	
	//private static ClipboardContent content = new ClipboardContent();
	
	private static MainModel model;
	
	private static ProxyModel proxyModel;
	
	private static SitesModel sitesModel;
	
	private static BarModel barModel;
	
	
	private ProxyTableController proxyTableController;
	
	private SitesController sitesController;
	
	private ProxyController proxyController;
	
	private IOController inOutController;
	
	private SiteTableController siteTableController;
	
	private BarController barController;
	
	
	public static void main(String[] args) {
		
		model = new MainModel();
		
		if (args.length == 0) {
			model.setVarsInterval();
			Interval.setInterval(100);
			proxyModel = new ProxyModel(model.getChecker());
			sitesModel = new SitesModel(model.getAssigner(), model.getScraper(), model.getGather(), model.getDataBase());
			barModel = new BarModel(model.getProxyManager(), model.getCheckOnFly());
			
			model.getProxyManager().setModel(proxyModel);
			model.getSitesManager().setModel(sitesModel);
			
			launch();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Proxy Scraper");
		
		FXMLLoader main = new FXMLLoader(getClass().getResource("/view/main.fxml"));
		BorderPane rootPane = main.load();
		main.setController(this);
		
		FXMLLoader bar = new FXMLLoader(getClass().getResource("/view/bar.fxml"));
		rootPane.setTop(bar.load());
		barController = bar.getController();
		
		TabPane tabPane = (TabPane) rootPane.getCenter();
		
		BorderPane proxyRoot = (BorderPane) tabPane.getTabs().get(0).getContent();
		BorderPane siteRoot = (BorderPane) tabPane.getTabs().get(1).getContent();
		
		FXMLLoader proxyTable = new FXMLLoader();
		proxyTable.setLocation(getClass().getResource("/view/proxyTable.fxml"));
		proxyRoot.setCenter(proxyTable.load());
		proxyTableController = proxyTable.getController();
		
		FXMLLoader proxyControl = new FXMLLoader(getClass().getResource("/view/proxyControl.fxml"));
		VBox proxyControllBox = proxyControl.load();
		proxyRoot.setRight(proxyControllBox);
		proxyController = proxyControl.getController();
		
		FXMLLoader inOut = new FXMLLoader(getClass().getResource("/view/IO.fxml"));
		proxyControllBox.getChildren().add(inOut.load());
		inOutController = inOut.getController();
		
		FXMLLoader sitesControl = new FXMLLoader(getClass().getResource("/view/sitesControl.fxml"));
		siteRoot.setRight(sitesControl.load());
		sitesController = sitesControl.getController();
		
		FXMLLoader siteTable = new FXMLLoader(getClass().getResource("/view/sitesTable.fxml"));
		siteRoot.setCenter(siteTable.load());
		siteTableController = siteTable.getController();
		
		
		Scene scene = new Scene(rootPane);
		
		scene.getStylesheets().add(String.valueOf(getClass().getResource("/view/userAgent.css")));
		//scene.setUserAgentStylesheet(String.valueOf(getClass().getResource("/userAgent.css")));
		
		barController.initialize(barModel);
		
		sitesController.initialize(sitesModel, model.getSitesManager(), siteTableController.getTable());
		proxyController.initialize(proxyModel, proxyTableController.getTable());
		
		siteTableController.initialize(sitesModel);
		proxyTableController.initialize(proxyModel);
		
		
		
		//proxyTableController.initialize();
		//proxyController.initialize();
		
		/*controller.setTextArea(editable);
		
		setFilters();
		setTextAreas();
		
		
		scrapeButton.setOnAction(event -> controller.scrapeConcurrent());
		
		editable.textProperty().addListener((observable, oldValue, newValue) ->
													   controller.updateCheck(newValue, notEditable));
		
		notEditable.scrollTopProperty().bindBidirectional(editable.scrollTopProperty());*/
		
		
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	
	/*public static void handleCopy(){
		content.clear();
		ProxyTableModel.getVisible().forEach(checkable -> {
			content.put(DataFormat.PLAIN_TEXT, checkable.getText()+"\n");
		});
	}
	
	public static void handlePaste(){
		List<String> pasteList = new ArrayList<>();
		
		String clipboardText = (String)clipboard.getContent(DataFormat.PLAIN_TEXT);
		if(clipboardText!=null){
			String[] clipboardTextArray = clipboardText.split("\\n");
			
			Arrays.stream(clipboardTextArray).forEach(ProxyTableModel::addProxy);
		}
	}*/
}
