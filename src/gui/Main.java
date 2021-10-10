package gui;

import java.io.File;

import com.amazonaws.transcribestreaming.TranscribeStreamingClientWrapper;
import com.amazonaws.transcribestreaming.TranscribeStreamingSynchronousClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
	private static TranscribeStreamingClientWrapper client;
	private static ParallelRunner parallelRunner;
	@Override
	public void start(Stage primaryStage) throws Exception {
		parallelRunner = new ParallelRunner();
		client = new TranscribeStreamingClientWrapper();

		new TranscribeStreamingSynchronousClient(TranscribeStreamingClientWrapper.getClient());


			parallelRunner.pollyDemo.mainPlay("Initializing Artificial Intelligence");

		Pane root =(Pane) FXMLLoader.load(getClass().getResource("Mainfx.fxml"));
		primaryStage.setTitle("Assistant App");
		Scene scene =new Scene(root);
		Image image = new Image(new File("background.gif").toURI().toString());
		ImageView imageView= new ImageView();
		imageView.setImage(image);
		root.getChildren().add(0,imageView);
		scene.getStylesheets().addAll(this.getClass().getResource("flatbee.css").toExternalForm());
		primaryStage.setScene(scene );

		primaryStage.show();

	}
	public static TranscribeStreamingClientWrapper getClient() {
		return client;
	}
	public static ParallelRunner getParallelRunner() {
		return parallelRunner;
	}


	public static void main(String[] args) {
		launch(args);
	}
}
