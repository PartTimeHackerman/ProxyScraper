package org.scraper.main.scraper.ocr;


@Deprecated
public class OpenCvFilter {
	/*
	static {
		//Load OpenCV lib
		String sysArch = System.getProperty("os.arch");
		sysArch = sysArch.substring(sysArch.length() - 2);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME + "x" + sysArch);
	}
	
	@Override
	public Image filterList(byte[] image) {
		Mat mat = Imgcodecs.imdecode(new MatOfByte(image), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		
		double sizeMult = 300 / (double) mat.height();
		
		resize(mat, mat, new Size((int) (mat.size().width * sizeMult), (int) (mat.size().height * sizeMult)));
		mat.convertTo(mat, 0, 2, -255);
		adaptiveThreshold(mat, mat, 255, ADAPTIVE_THRESH_GAUSSIAN_C, THRESH_BINARY, 255, 1);
		
		byte[] filtered = new byte[(int) (mat.total() * mat.channels())];
		mat.get(0, 0, filtered);
		return new Image(mat.width(), mat.height(), filtered, 1);
	}*/
}
