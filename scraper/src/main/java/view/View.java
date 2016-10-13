package view;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
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
	
	private static Clipboard clipboard = Clipboard.getSystemClipboard();
	
	private static ClipboardContent content = new ClipboardContent();
	
	
	private static MainModel model;
	
	private ProxyTableController textController;
	
	private ExecuteController executeController;
	
	private FilterController filterSortController;
	
	private IOController inOutController;
	
	private SiteTableController siteTableController;
	
	
	
	public static void main(String[] args) {
		
		model = new MainModel();
		
		if(args.length == 0)
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Ultimate Proxy Scraper");
		
		FXMLLoader main = new FXMLLoader(getClass().getResource("/view/main.fxml"));
		TabPane tabPane = main.load();
		main.setController(this);
		
		BorderPane proxyRoot = (BorderPane) tabPane.getTabs().get(0).getContent();
		BorderPane siteRoot = (BorderPane) tabPane.getTabs().get(1).getContent();
		
		FXMLLoader proxyTable = new FXMLLoader();
		proxyTable.setLocation(getClass().getResource("/view/proxyTable.fxml"));
		proxyRoot.setCenter(proxyTable.load());
		textController = proxyTable.getController();
		
		FXMLLoader execute = new FXMLLoader(getClass().getResource("/view/execute.fxml"));
		proxyRoot.setBottom(execute.load());
		executeController = execute.getController();
		
		FXMLLoader filterSort = new FXMLLoader(getClass().getResource("/view/filters.fxml"));
		proxyRoot.setRight(filterSort.load());
		filterSortController = filterSort.getController();
		
		FXMLLoader inOut = new FXMLLoader(getClass().getResource("/view/IO.fxml"));
		proxyRoot.setLeft(inOut.load());
		inOutController = inOut.getController();
		
		FXMLLoader siteTable = new FXMLLoader(getClass().getResource("/view/siteTable.fxml"));
		siteRoot.setLeft(siteTable.load());
		siteTableController = siteTable.getController();
		
		
		
		Scene scene = new Scene(tabPane);
		
		scene.getStylesheets().add(String.valueOf(getClass().getResource("/view/userAgent.css")));
		//scene.setUserAgentStylesheet(String.valueOf(getClass().getResource("/userAgent.css")));
		
		
		
		executeController.initialize(model);
		//textController.initialize();
		//filterSortController.initialize();
		
		/*controller.setTextArea(editable);
		
		setFilters();
		setTextAreas();
		
		
		scrapeButton.setOnAction(event -> controller.scrape());
		
		editable.textProperty().addListener((observable, oldValue, newValue) ->
													   controller.updateCheck(newValue, notEditable));
		
		notEditable.scrollTopProperty().bindBidirectional(editable.scrollTopProperty());*/
		
		
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
