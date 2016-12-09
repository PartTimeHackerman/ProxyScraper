package org.scraper.model;

import java.io.*;

public class TempFileManager {
	
	public static <T> File loadResource(Class<T> clazz, String name ){
		File file = null;
		InputStream in = clazz.getResourceAsStream(name);
		try {
			file = new File(System.getProperty("user.dir")+"/"+name);//File.createTempFile("scraper_", name);
			new File(file.getParent()).mkdirs();
			
			
			file.deleteOnExit();
			try (FileOutputStream out = new FileOutputStream(file)) {
				//copy stream
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
			
			return file;
		} catch (IOException e) {e.printStackTrace();
			return null;
		}
	}
}
