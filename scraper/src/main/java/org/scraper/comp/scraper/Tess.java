package org.scraper.comp.scraper;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.opencv.core.Mat;

import static org.bytedeco.javacpp.lept.pixRead;

public class Tess {


	public static String read(Object image) {

		TessBaseAPI api = new TessBaseAPI();

		api.SetVariable("m_data_sub_dir", System.getProperty("user.dir")+"\\");
		api.SetVariable("tessedit_char_whitelist", "0123456789.:");
		String path = System.getProperty("user.dir");

		if (api.Init(path, "eng") != 0) {
			System.err.println("Could not initialize tesseract.");
		}

		if(image instanceof String){
			api.SetImage(pixRead((String) image));
		}
		if(image instanceof Mat){
			Mat matImg = (Mat) image;
			byte[] buff = new byte[(int) (matImg.total() *
					matImg.channels())];
			matImg.get(0, 0, buff);
			api.SetImage(buff,matImg.width(), matImg.height(), matImg.channels(), (int) matImg.step1());
		}
		String outText = api.GetUTF8Text().getString().replace(" ","");

		api.End();

		return outText;
	}
}