package view;

import javafx.fxml.FXML;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.VBox;
import org.scraper.control.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.scraper.model.managers.ProxyTableManager;
import org.scraper.model.modles.MainModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class View extends Application {
	
	@FXML
	private Label logBar;
	
	private static Clipboard clipboard = Clipboard.getSystemClipboard();
	
	
	private static ClipboardContent content = new ClipboardContent();
	
	private static MainModel model;
	
	private ProxyTableController proxyTableController;
	
	private SitesController sitesController;
	
	private ProxyController proxyController;
	
	private IOController inOutController;
	
	private SiteTableController siteTableController;
	
	private BarController barController;
	
	
	
	public static void main(String[] args) {
		
		model = new MainModel();
		
		//TEST
		model.getSitesManager().addSites(model.getDataBase().getAllSites());
		
		if(args.length == 0)
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Ultimate Proxy Scraper");
		
		
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
		
		
		
		sitesController.initialize(model);
		siteTableController.initialize(model.getSitesManager());
		barController.initialize(model);
		
		//proxyTableController.initialize();
		//proxyController.initialize();
		
		/*controller.setTextArea(editable);
		
		setFilters();
		setTextAreas();
		
		
		scrapeButton.setOnAction(event -> controller.scrape());
		
		editable.textProperty().addListener((observable, oldValue, newValue) ->
													   controller.updateCheck(newValue, notEditable));
		
		notEditable.scrollTopProperty().bindBidirectional(editable.scrollTopProperty());*/
		
		
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	
	public static void handleCopy(){
		content.clear();
		ProxyTableManager.getVisible().forEach(checkable -> {
			content.put(DataFormat.PLAIN_TEXT, checkable.getText()+"\n");
		});
	}
	
	public static void handlePaste(){
		List<String> pasteList = new ArrayList<>();
		
		String clipboardText = (String)clipboard.getContent(DataFormat.PLAIN_TEXT);
		if(clipboardText!=null){
			String[] clipboardTextArray = clipboardText.split("\\n");
			
			Arrays.stream(clipboardTextArray).forEach(ProxyTableManager::addProxy);
		}
	}
}
