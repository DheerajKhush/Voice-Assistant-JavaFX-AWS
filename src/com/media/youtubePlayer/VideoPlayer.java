package com.media.youtubePlayer;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class VideoPlayer {
	Stage primaryStage;

	 public static void showVideo() {
		Runnable task=()->{
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				WebView webview = new WebView();
			    webview.getEngine().load(
			      "https://www.youtube.com/feed/storefront?bp=kgEDCOICogUCKAU%3D"
			    );
			    webview.setPrefSize(700, 500);

			StackPane secondaryLayout = new StackPane();
			secondaryLayout.getChildren().add(webview);

			Scene secondScene = new Scene(secondaryLayout);

			// New window (Stage)
			Stage newWindow = new Stage();
			newWindow.setTitle("Movie Player");
			newWindow.setScene(secondScene);


			newWindow.show();


			}
		});
		};
		Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

	}



}