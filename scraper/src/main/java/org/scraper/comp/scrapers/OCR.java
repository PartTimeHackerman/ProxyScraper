package org.scraper.comp.scrapers;

import org.bytedeco.javacpp.tesseract;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.opencv.core.Mat;

import static org.bytedeco.javacpp.lept.pixRead;

public class OCR {

	private TessBaseAPI api = new TessBaseAPI();

	public OCR(){
		if (api.Init(System.getProperty("user.dir"), "eng", tesseract.OEM_DEFAULT) != 0) {
			System.err.println("Could not initialize tesseract.");
		}
		//api.SetPageSegMode(tesseract.PSM_AUTO_OSD);
		api.SetVariable("m_data_sub_dir", System.getProperty("user.dir")+"\\");
		api.SetVariable("tessedit_char_whitelist", "0123456789.:");
		api.SetVariable("tessedit_char_blacklist", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmopqrstuvwxyz?!$^()[]{};><‘'\"\\|");

	}

	public String read(Object image) {

		if(image instanceof String){
			api.SetImage(pixRead((String) image));
		}

		if(image instanceof Mat){
			Mat matImg = (Mat) image;
			byte[] buff = new byte[(int) (matImg.total() * matImg.channels())];
			matImg.get(0, 0, buff);
			api.SetImage(buff,matImg.width(), matImg.height(), matImg.channels(), (int) matImg.step1());
		}

		String text =  api.GetUTF8Text()
				.getString()
				.replace(" ","")
				.replace("\n","");
		//shutdown();
		return text;

	}

	public void shutdown(){
		api.End();
	}
}