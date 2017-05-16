package org.scraper.view.control;

import scraper.MainLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface ISaveable {
	String lineSeparator = System.getProperty("line.separator");
	
	void save(File file);
	
	default void saveFile(List<String> lines, File file) {
		try {
			FileWriter fileWriter = new FileWriter(file);
			for (String line : lines) {
				fileWriter.write(line);
				fileWriter.write(lineSeparator);
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			MainLogger.log(this).fatal("Failed to save a file {}!", file.getName());
		}
	}
	
	
}
