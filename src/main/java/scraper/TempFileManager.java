package scraper;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

public class TempFileManager {
	
	public static <T> File loadResource(Class<T> clazz, String name) {
		File file = null;
		InputStream in = clazz.getResourceAsStream(name);
		file = new File(System.getProperty("user.dir") + "/" + name);//File.createTempFile("scraper_", name);
		if (file.exists())
			return file;
		
		new File(file.getParent()).mkdirs();
		file.deleteOnExit();
		
		System.out.println(file.getAbsolutePath() +"\n" + clazz.getResource(name));
		write(in, file);
		
		return file;
	}
	
	public static <T> File loadDir(Class<T> clazz, String name) {
		File dstt = null;
		try {
			final File src = new File(clazz.getResource(name).getFile());
			
			final File dst = new File(System.getProperty("user.dir") + "/" + name);
			dstt = dst;
			if (dst.exists())
				return dst;
			dst.mkdirs();
			
			URI uri = clazz.getResource(name).toURI();
			
			try (FileSystem fileSystem = (uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap()) : null)) {
				Path myPath = Paths.get(uri);
				Files.walkFileTree(myPath, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						String f11 = file.toString().replace('\\','/');
						String[] f = f11.split("/");
						String f1 = f[f.length-1];
						write(clazz.getResourceAsStream("plugins/"+f1), new File(dst.getAbsolutePath() + "/" + f1));
						return FileVisitResult.CONTINUE;
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return dstt;
	}
	
	public static void write(InputStream in, File file) {
		try (FileOutputStream out = new FileOutputStream(file)) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			return;
		}
	}
	
	
}
