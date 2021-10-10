package gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class videoMain extends Application {

	@Override
	public void start(Stage primaryStage) {

		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("videoFxml.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		primaryStage.setTitle("Assistant App");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

	}


	public static void main(String[] args) {
		launch(args);
	}
}
