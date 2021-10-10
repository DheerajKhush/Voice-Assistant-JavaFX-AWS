package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.amazonaws.transcribestreaming.StreamTranscriptionBehavior;
import com.amazonaws.transcribestreaming.TranscribeStreamingClientWrapper;
import com.amazonaws.transcribestreaming.TranscribeStreamingSynchronousClient;
import com.media.youtubePlayer.VideoPlayer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import software.amazon.awssdk.services.transcribestreaming.model.Result;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionResponse;
import software.amazon.awssdk.services.transcribestreaming.model.TranscriptEvent;
import software.amazon.awssdk.services.transcribestreaming.model.TranscriptResultStream;

public class MainfxController implements Initializable {
	private ParallelRunner parallelRunner;
	private TranscribeStreamingClientWrapper client;
	private CompletableFuture<Void> inProgressStreamingRequest;
	private String finalTranscript = "";

	@FXML
	private TextArea outputTextArea;
	@FXML
	private Button StartStopMicButton, browserButton;
	
	Stage primaryStage;
	@FXML
	private WebView webViewBrowser;
	@FXML
	MediaView mediaView;
	@FXML
	private ImageView imageView;

	// Event Listener on Button[#StartStopMicButton].onAction
	Thread threadMicrophone;
	@FXML
	public void startMic(ActionEvent event) {
		startTranscriptionRequest(null);
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		
//		Runnable task=()->{
//			Platform.runLater(new Runnable() {
//
//				@Override
//				public void run() {
					
					parallelRunner = Main.getParallelRunner();
					client=Main.getClient();
					StartStopMicButton.fire();
				
//					Image image = new Image(new File("background.gif").toURI().toString());
//					imageView.setImage(image);					
					
//				}
//
//					
//				});
//			};
//			Thread thread = new Thread(task);
//			thread.setDaemon(true);
//			thread.start();


	}

		
		


	public void close() {
		if (inProgressStreamingRequest != null) {
			inProgressStreamingRequest.completeExceptionally(new InterruptedException());
		}
		client.close();
	}

	private void startTranscriptionRequest(File inputFile) {
		if (inProgressStreamingRequest == null) {

			finalTranscript = "";
			StartStopMicButton.setText("Connecting...");
			StartStopMicButton.setDisable(true);
			outputTextArea.clear();

			// saveButton.setDisable(true);
			inProgressStreamingRequest = client.startTranscription(getResponseHandlerForWindow());
		}
	}

	public void stopTranscription() {
		if (inProgressStreamingRequest != null) {
			try {

				client.stopTranscription();
				inProgressStreamingRequest.get();
			} catch (ExecutionException | InterruptedException e) {
				System.out.println("error closing stream");
			} finally {
				inProgressStreamingRequest = null;
				StartStopMicButton.setText("Start Microphone Transcription");
				StartStopMicButton.setOnAction(__ -> startTranscriptionRequest(null));
				StartStopMicButton.setDisable(false);
			}

		}
	}

	private StreamTranscriptionBehavior getResponseHandlerForWindow() {
		return new StreamTranscriptionBehavior() {

			// This will handle errors being returned from AWS Transcribe in your response.
			// Here we just print the exception.
			@Override
			public void onError(Throwable e) {
				System.out.println(e.getMessage());
				Throwable cause = e.getCause();
				while (cause != null) {
					System.out.println("Caused by: " + cause.getMessage());
					Arrays.stream(cause.getStackTrace()).forEach(l -> System.out.println("  " + l));
					if (cause.getCause() != cause) { // Look out for circular causes
						cause = cause.getCause();
					} else {
						cause = null;
					}
				}
				System.out.println("Error Occurred: " + e);
			}

			@Override
			public void onStream(TranscriptResultStream event) {
				List<Result> results = ((TranscriptEvent) event).transcript().results();
				if (results.size() > 0) {

					Result firstResult = results.get(0);
					if (firstResult.alternatives().size() > 0
							&& !firstResult.alternatives().get(0).transcript().isEmpty()) {

						String transcript = firstResult.alternatives().get(0).transcript();
						if (!transcript.isEmpty()) {
							System.out.println(transcript);
							try {
								if (!firstResult.isPartial() && (firstResult.endTime() - firstResult.startTime() > 0.5))
									performAction(transcript);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String displayText;
							if (!firstResult.isPartial()) {
								finalTranscript = transcript + " ";
								displayText = finalTranscript;
							} else {
								displayText = finalTranscript + " " + transcript;
							}
							Platform.runLater(() -> {
								outputTextArea.clear();
								outputTextArea.setText(displayText);

							});
						}
					}

				}
			}

			@Override
			public void onResponse(StartStreamTranscriptionResponse r) {
				System.out.println(String.format("=== Received Initial response. Request Id: %s ===", r.requestId()));
				Platform.runLater(() -> {
					StartStopMicButton.setText("Stop Transcription");
					StartStopMicButton.setOnAction(__ -> stopTranscription());
					StartStopMicButton.setDisable(false);
				});
			}

			@Override
			public void onComplete() {
				System.out.println("=== All records streamed successfully ===");

			}
		};
	}

	String lastCommand = "";

	protected void performAction(String transcript) throws IOException {
		String resultStr = transcript.toLowerCase();
		
		if (resultStr.contains("hello") || resultStr.contains("hello maya.")) {

			String currentCommand = "hello";
			if (!currentCommand.equals(lastCommand)) {
				parallelRunner.helloCommand();
				lastCommand = currentCommand;
			}

		}else {

		if ((resultStr.contains("open a browser") || resultStr.contains("open browser")
				|| resultStr.contains("open google chrome") || resultStr.contains("open google chrome browser."))) {

			String currentCommand = "Chrome browser";
			if (!currentCommand.equals(lastCommand)) {
				parallelRunner.startBrowser();
				lastCommand = currentCommand;
			}

		} else if ((resultStr.contains("open note pad") || resultStr.contains("open, a text editors.")
				|| resultStr.contains("open notbook.") || resultStr.contains("open text editor."))) {

			String currentCommand = "notepad";
			if (!currentCommand.equals(lastCommand)) {
				parallelRunner.startNotepad();
				lastCommand = currentCommand;
			}

		} else if ((resultStr.contains("search") || resultStr.contains("search about")
				|| resultStr.contains("search for"))) {

			int searchIdx = resultStr.lastIndexOf("search", 0);

			String currentCommand = "search " + resultStr.substring(searchIdx + 6);
			if (!currentCommand.equals(lastCommand)) {
				parallelRunner.startSearching(resultStr.substring(searchIdx + 6));
				lastCommand = currentCommand;
			}

		} else if ((resultStr.contains("who") || resultStr.contains("what") || resultStr.contains("where"))) {

			String currentCommand = "search " + resultStr;
			if (!currentCommand.equals(lastCommand)) {
				parallelRunner.startSearchingFor(resultStr);
				lastCommand = currentCommand;
			}

		} else if (resultStr.contains("play a movie") || resultStr.contains("movie time")
				|| resultStr.contains("play movie") || resultStr.contains("play a movie on youtube")) {

			String currentCommand = "movie " + resultStr;
			if (!currentCommand.equals(lastCommand)) {
				
				VideoPlayer.showVideo();
				
				parallelRunner.playMovie();
				lastCommand = currentCommand;
			}

		}else if (resultStr.contains("stop")) {

			String currentCommand = "stop " + resultStr;
			if (!currentCommand.equals(lastCommand)) {
				
				StartStopMicButton.fire();
			}

		}
		
	}
	


}
