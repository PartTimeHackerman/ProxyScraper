package org.scraper.MVC;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.scraper.MVC.control.*;
import org.scraper.MVC.model.*;
import org.scraper.main.Interval;
import org.scraper.main.MainLogger;


public class View extends Application {
	
	@FXML
	private Label logBar;
	
	private static MainModel model;
	
	private static ProxyModelFX proxyModel;
	
	private static SitesModelFX sitesModel;
	
	private static GeneralOptions generalOptions;
	
	
	private ProxyTableController proxyTableController;
	
	private SitesController sitesController;
	
	private ProxyController proxyController;
	
	private IOController proxyInOutController;
	
	private IOController siteInOutController;
	
	private SiteTableController sitesTableController;
	
	private BarController barController;
	
	
	public static void main(String[] args) {
		
		model = new MainModel();
		
		if (args.length == 0) {
			model.setVarsInterval();
			Interval.setInterval(100);
			proxyModel = new ProxyModelFX(model.getChecker(), model.getPool());
			sitesModel = new SitesModelFX(model.getAssigner(), model.getScraper(), model.getGather(), model.getPool());
			generalOptions = new GeneralOptions(model.getProxyRepo(), model.getCheckOnFly(), model.getLimiter(), model.getPool());
			
			model.getProxyRepo().setModel(proxyModel);
			model.getSitesRepo().setModel(sitesModel);
			
			launch();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Proxy Scraper");
		
		MainLogger.out().addConsumer(log -> Platform.runLater(() -> logBar.setText(log)));
		
		FXMLLoader main = new FXMLLoader(getClass().getResource("control/view.fxml"));
		main.setController(this);
		BorderPane rootPane = main.load();
		
		
		TabPane tabPane = (TabPane) rootPane.getCenter();
		
		BorderPane proxyRoot = (BorderPane) tabPane.getTabs().get(0).getContent();
		BorderPane siteRoot = (BorderPane) tabPane.getTabs().get(1).getContent();
		
		
		FXMLLoader bar = new FXMLLoader(getClass().getResource("control/bar.fxml"));
		rootPane.setTop(bar.load());
		barController = bar.getController();
		
		
		FXMLLoader proxyTable = new FXMLLoader();
		proxyTable.setLocation(getClass().getResource("control/proxyTable.fxml"));
		Node proxyTableNode = proxyTable.load();
		//BorderPane.setAlignment(proxyTableNode, Pos.TOP_LEFT);
		proxyRoot.setCenter(proxyTableNode);
		proxyTableController = proxyTable.getController();
		
		FXMLLoader proxyControl = new FXMLLoader(getClass().getResource("control/proxyControl.fxml"));
		VBox proxyControllBox = proxyControl.load();
		BorderPane.setAlignment(proxyControllBox, Pos.TOP_LEFT);
		proxyRoot.setRight(proxyControllBox);
		proxyController = proxyControl.getController();
		
		FXMLLoader proxyInOut = new FXMLLoader(getClass().getResource("control/IO.fxml"));
		proxyControllBox.getChildren().add(proxyInOut.load());
		proxyInOutController = proxyInOut.getController();
		
		
		FXMLLoader siteTable = new FXMLLoader(getClass().getResource("control/sitesTable.fxml"));
		siteRoot.setCenter(siteTable.load());
		sitesTableController = siteTable.getController();
		
		FXMLLoader sitesControl = new FXMLLoader(getClass().getResource("control/sitesControl.fxml"));
		VBox sitesControllBox = sitesControl.load();
		siteRoot.setRight(sitesControllBox);
		sitesController = sitesControl.getController();
		
		FXMLLoader sitesInOut = new FXMLLoader(getClass().getResource("control/IO.fxml"));
		sitesControllBox.getChildren().add(sitesInOut.load());
		siteInOutController = sitesInOut.getController();
		
		
		Scene scene = new Scene(rootPane);
		
		scene.getStylesheets().add(String.valueOf(getClass().getResource("control/userAgent.css")));
		
		barController.initialize(generalOptions);
		
		sitesController.initialize(sitesModel, sitesTableController);
		proxyController.initialize(proxyModel, proxyTableController);
		
		sitesTableController.initialize(sitesModel);
		proxyTableController.initialize(proxyModel);
		
		proxyInOutController.initialize(proxyController);
		siteInOutController.initialize(sitesController);
		
		
		primaryStage.setOnCloseRequest(e -> System.exit(0));
		
		
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//primaryStage.setMaxWidth(rootPane.getWidth());
		//primaryStage.setMinWidth(rootPane.getWidth());
		//primaryStage.setMinHeight(rootPane.getHeight());
		
	}
	
	
	
}
