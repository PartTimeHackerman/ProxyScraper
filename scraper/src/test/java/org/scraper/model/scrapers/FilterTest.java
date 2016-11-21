package org.scraper.model.scrapers;

import org.junit.BeforeClass;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class FilterTest {
	
	protected static final String expected = "97.77.104.22";
	
	protected static byte[] image;
	
	@BeforeClass
	public static void setUpClass() throws IOException {
		BufferedImage bufferedImage = ImageIO.read(new File("97-77-104-22.png"));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "bmp", outputStream );
		outputStream.flush();
		image = outputStream.toByteArray();
		outputStream.close();
	}
}