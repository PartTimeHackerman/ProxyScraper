package org.scraper.model.scraper;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.*;


public class Window extends Application {

	@FXML
	private Slider slider1;
	@FXML
	private Slider slider2;
	@FXML
	private Slider slider3;

	@FXML
	private ImageView img1;
	@FXML
	private ImageView img2;

	@FXML
	private Label val1;
	@FXML
	private Label val2;
	@FXML
	private Label val3;

	@FXML
	private Label ip;

	@FXML
	private Button thresh;
	@FXML
	private Button convert;
	@FXML
	private Button newI;
	@FXML
	private Button resize;

	private Integer value1 = 0;
	private Integer value2 = 0;
	private Integer value3 = 0;

	private String firstImage = "D:/proxy.png";
	private String secondImage = "D:/img2.png";
	private String tiffImage = "D:/img2.tiff";

	private Mat image = imread(firstImage, CV_LOAD_IMAGE_GRAYSCALE);

	private Thresh threshF = new Thresh();
	private Convert convertF = new Convert();
	private Filter filter = convertF;

	@Override
	public void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/window.fxml"));
		loader.setController(this);
		BorderPane root = (BorderPane) loader.load();


		Scene scene = new Scene(root, 600, 600);

		primaryStage.setTitle("Test");
		primaryStage.setScene(scene);
		primaryStage.show();

		img1.setImage(new Image("file:///" + firstImage));


		thresh.setOnAction(e -> {
			filter = threshF;
			image = imread(secondImage, CV_LOAD_IMAGE_GRAYSCALE);
		});

		convert.setOnAction(e -> {
			filter = convertF;
			image = imread(secondImage, CV_LOAD_IMAGE_GRAYSCALE);
		});

		newI.setOnAction(e -> {
			image = imread(firstImage, CV_LOAD_IMAGE_GRAYSCALE);
		});

		resize.setOnAction(e -> {
			resize(image, image, new Size((int) (image.size().width * 2), (int) (image.size().height * 2)));
			write(image);
			update();
		});


		slider1.valueProperty().addListener((observable, oldValue, newValue) -> {
			value1 = newValue.intValue();
			val1.setText("" + value1);

			filter.execute(image, value1, value2, value3);

			update();
		});

		slider2.valueProperty().addListener((observable, oldValue, newValue) -> {
			value2 = newValue.intValue();
			val2.setText("" + value2);

			filter.execute(image, value1, value2, value3);

			update();
		});

		slider3.valueProperty().addListener((observable, oldValue, newValue) -> {
			value3 = newValue.intValue();
			val3.setText("" + value3);

			filter.execute(image, value1, value2, value3);

			update();
		});
	}


	public static void main() {
		launch();
	}

	private interface Filter {
		void execute(Mat image, Integer... args);
	}

	private void update() {
		img2.setImage(new Image("file:///" + secondImage));
		ip.setText(new OCR().read(tiffImage));
	}

	private void write(Mat image) {
		imwrite(secondImage, image);
		imwrite(tiffImage, image);
	}

	private class Thresh implements Filter {
		@Override
		public void execute(Mat image, Integer... args) {
			Mat image2 = new Mat();
			adaptiveThreshold(image, image2, args[2], ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, args[1], args[0]);
			write(image2);
		}
	}

	private class Convert implements Filter {
		@Override
		public void execute(Mat image, Integer... args) {
			Mat image2 = new Mat();
			image.convertTo(image2, args[0], args[1], args[2]);
			write(image2);
		}
	}
}
