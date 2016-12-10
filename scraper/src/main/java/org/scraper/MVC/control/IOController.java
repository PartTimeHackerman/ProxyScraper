package org.scraper.MVC.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class IOController {
	
	@FXML
	private Button save;
	
	@FXML
	private Button load;
	
	private FileChooser chooser;
	
	private Stage tempStage = new Stage();
	
	private ISaveable saver;
	
	private ILoader loader;
	
	private File initialDir = new File(".");
	
	@FXML
	public <T extends ISaveable & ILoader> void initialize(T saveAndLoadable) {
		initialize(saveAndLoadable, saveAndLoadable);
	}
	
	@FXML
	public void initialize(ISaveable saver, ILoader loader) {
		this.saver = saver;
		this.loader = loader;
		chooser = new FileChooser();
		chooser.setInitialDirectory(initialDir);
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
		
		save.setOnAction(event ->
								 saveFile());
		load.setOnAction(event ->
								 loadFile());
	}
	
	private void saveFile() {
		chooser.setTitle("Save file");
		File choosen = chooser.showSaveDialog(tempStage);
		if (choosen != null)
			saver.save(choosen);
	}
	
	private void loadFile() {
		chooser.setTitle("Load file");
		File choosen = chooser.showOpenDialog(tempStage);
		if (choosen != null)
			loader.load(choosen);
	}
}
