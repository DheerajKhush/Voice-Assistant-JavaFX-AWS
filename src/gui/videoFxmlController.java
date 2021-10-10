package gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class videoFxmlController implements Initializable {

	@FXML
	private AnchorPane anchorPane;
	@FXML
	ImageView imageView;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image=new Image(new File("background.gif").toURI().toString());
		imageView.setImage(image);



	}
}
