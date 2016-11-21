package org.scraper.model.scrapers.ocr;

import marvin.MarvinDefinitions;
import marvin.image.MarvinImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static marvin.MarvinPluginCollection.scale;
import static marvin.MarvinPluginCollection.thresholding;

public class MarvinFilter implements IOcrFilter {
	static {
		MarvinDefinitions.setImagePluginPath(new File("").getAbsolutePath() + "\\marvin\\plugins\\");
	}
	
	@Override
	public Image filter(byte[] image) {
		try {
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
			MarvinImage marvinImage = new MarvinImage(bufferedImage);
			
			double sizeMult = 300 / (double) marvinImage.getHeight();
			scale(marvinImage.clone(), marvinImage,(int) (marvinImage.getWidth() * sizeMult),(int) (marvinImage.getHeight() * sizeMult));
			
			//grayScale(marvinImage.clone(), marvinImage);
			
			//gaussianBlur(marvinImage.clone(), marvinImage, 3);
			
			thresholding(marvinImage.clone(), marvinImage, 125);
			
			int[] values = marvinImage.getIntColorArray();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			for(int i=0; i < values.length; ++i)
			{
				dos.writeInt(values[i]);
			}
			
			byte[] resultBytes = baos.toByteArray();
			return new Image(marvinImage.getWidth(), marvinImage.getHeight(), resultBytes, 4);
			
		} catch (IOException e) {
			return new Image(1, 1, new byte[1], 1);
		}
	}
}