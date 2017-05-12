package org.scraper.MVC.control;

import org.scraper.main.MainLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ILoader {
	
	void load(File file);
	
	default List<String> readFile(File file) {
		List<String> loadedLines = new ArrayList<>();
		BufferedReader bufferedReader = null;
		
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			
			String text;
			while ((text = bufferedReader.readLine()) != null) {
				loadedLines.add(text);
			}
			
		} catch (IOException e) {
			MainLogger.log(this).fatal("Failed to load a file {}!", file.getName());
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception e) {
				MainLogger.log(this).fatal("Failed to close a file {}!", file.getName());
			}
		}
		
		return loadedLines;
	}
}
