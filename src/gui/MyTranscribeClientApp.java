package gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.amazonaws.polly.PollyDemo;
import com.amazonaws.transcribestreaming.StreamTranscriptionBehavior;
import com.amazonaws.transcribestreaming.TranscribeStreamingClientWrapper;
import com.amazonaws.transcribestreaming.TranscribeStreamingSynchronousClient;

import software.amazon.awssdk.services.transcribestreaming.model.Result;
import software.amazon.awssdk.services.transcribestreaming.model.StartStreamTranscriptionResponse;
import software.amazon.awssdk.services.transcribestreaming.model.TranscriptEvent;
import software.amazon.awssdk.services.transcribestreaming.model.TranscriptResultStream;

public class MyTranscribeClientApp {
	private static TranscribeStreamingClientWrapper client;
	private CompletableFuture<Void> inProgressStreamingRequest;
	private String finalTranscript = "";
	PollyDemo pollyDemo;

	public MyTranscribeClientApp() {
		client = new TranscribeStreamingClientWrapper();
		new TranscribeStreamingSynchronousClient(TranscribeStreamingClientWrapper.getClient());
		pollyDemo = new PollyDemo();

	}

	public static void main(String[] args) throws InterruptedException {
		MyTranscribeClientApp myTranscribeClientApp = new MyTranscribeClientApp();
		myTranscribeClientApp.startTranscriptionRequest();
		Thread.currentThread().join();
	}

	private void startTranscriptionRequest() {
		if (inProgressStreamingRequest == null) {

			inProgressStreamingRequest = client.startTranscription(getResponseHandlerForWindow());
			System.out.println("Listening :");
		}
	}

	private void stopTranscription() {
		if (inProgressStreamingRequest != null) {
			try {

				client.stopTranscription();
				inProgressStreamingRequest.get();
			} catch (ExecutionException | InterruptedException e) {
				System.out.println("error closing stream");
			} finally {
				inProgressStreamingRequest = null;

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
								performAction(transcript);
							} catch (IOException |InterruptedException e) {
								e.printStackTrace();
							}
							if (!firstResult.isPartial()) {
								finalTranscript = transcript + " ";

							}

						}
					}

				}
			}

			@Override
			public void onResponse(StartStreamTranscriptionResponse r) {
				System.out.println(String.format("=== Received Initial response. Request Id: %s ===", r.requestId()));
			}

			@Override
			public void onComplete() {
				System.out.println("=== All records streamed successfully ===");

			}
		};
	}

	protected void performAction(String transcript) throws IOException, InterruptedException {
		String resultStr = transcript.toLowerCase();
		if ((resultStr.contains("open browser") || resultStr.contains("open google chrome")
				|| resultStr.contains("open google chrome")) ) {

			System.out.println("Command: Open Browser");
			pollyDemo.setTextToPlay("Opening google chrome");
			try {
				pollyDemo.mainPlay("Opening google chrome");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome "});


		} else if (resultStr.contains("open note pad")) {

			System.out.println("Command: Open note pad");
			pollyDemo.setTextToPlay("Opening notepad text editor");
			Thread t= new Thread(pollyDemo);
			t.start();
			System.out.println("-----");
			Runtime.getRuntime().exec("notepad.exe");

		}

		if (transcript.toLowerCase().contains("stop")) {
			stopTranscription();
			client.close();
			System.exit(0);
		}


	}
}
