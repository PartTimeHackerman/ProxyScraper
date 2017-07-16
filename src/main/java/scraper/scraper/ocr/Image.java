package scraper.scraper.ocr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Image {
	
	private Integer width;
	
	private Integer height;
	
	private byte[] bytes;
	
	private Integer bytesPerPixel;
	
	public Image(byte[] bytes) {
		InputStream in = new ByteArrayInputStream(bytes);
		this.bytes = bytes;
		BufferedImage image = null;
		try {
			image = ImageIO.read(in);
		} catch (IOException ignored) {
		}
		
		width = image.getWidth();
		height = image.getHeight();
	}
	
	public Image(Integer width, Integer height, byte[] bytes, Integer bytesPerPixel) {
		this.width = width;
		this.height = height;
		this.bytes = bytes;
		this.bytesPerPixel = bytesPerPixel;
	}
	
	public Integer getWidth() {
		return width;
	}
	
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	public Integer getHeight() {
		return height;
	}
	
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public Integer getBytesPerPixel() {
		return bytesPerPixel;
	}
	
	public void setBytesPerPixel(Integer bytesPerPixel) {
		this.bytesPerPixel = bytesPerPixel;
	}
	
	public Integer getBytesPerLine() {
		return bytesPerPixel * width;
	}
}
