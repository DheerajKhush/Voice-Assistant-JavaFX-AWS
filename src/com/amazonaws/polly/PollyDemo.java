package com.amazonaws.polly;


import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPollyClient;
import com.amazonaws.services.polly.model.DescribeVoicesRequest;
import com.amazonaws.services.polly.model.DescribeVoicesResult;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.Voice;
import com.amazonaws.services.polly.model.VoiceId;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class PollyDemo implements Runnable {
	String textToPlay="";
	public void setTextToPlay(String textToPlay) {
		this.textToPlay = textToPlay;
	}

	@Override
	public void run() {
		if(textToPlay!=null)
			try {
				mainPlay(textToPlay);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

	private final AmazonPollyClient polly;
	private final Voice voice;
	private static final String SAMPLE = "Congratulations. You have successfully built this working demo"+
	"of Amazon Polly in Java. Have fun building voice enabled apps with Amazon Polly (that's me!), and always"+
	"look at the AWS website for tips and tricks on using Amazon Polly and other great services from AWS";

	public PollyDemo() {
		// create an Amazon Polly client in a specific region
		polly = new AmazonPollyClient(new DefaultAWSCredentialsProviderChain(),
		new ClientConfiguration());
		polly.setRegion(Region.getRegion(Regions.US_WEST_2));
		// Create describe voices request.
		DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();

		// Synchronously ask Amazon Polly to describe available TTS voices.
		DescribeVoicesResult describeVoicesResult = polly.describeVoices(describeVoicesRequest);
		voice = describeVoicesResult.getVoices().get(0);
	}

	public InputStream synthesize(String text, OutputFormat format) throws IOException {
		SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest().withText(text).withVoiceId(voice.getId())
				.withOutputFormat(format);
		synthReq.setVoiceId(VoiceId.Raveena);
		SynthesizeSpeechResult synthRes = polly.synthesizeSpeech(synthReq);

		return synthRes.getAudioStream();
	}

	public static void main(String args[]) throws Exception {
		//create the test class
		PollyDemo helloWorld = new PollyDemo();
		//get the audio stream
		InputStream speechStream = helloWorld.synthesize(SAMPLE, OutputFormat.Mp3);

		//create an MP3 player
		AdvancedPlayer player = new AdvancedPlayer(speechStream,
				javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());

		player.setPlayBackListener(new PlaybackListener() {
			@Override
			public void playbackStarted(PlaybackEvent evt) {
				System.out.println("Playback started");
				System.out.println(SAMPLE);
			}

			@Override
			public void playbackFinished(PlaybackEvent evt) {
				System.out.println("Playback finished");
			}
		});


		// play it!
		player.play();

	}
	public void mainPlay(String voiceText) throws Exception {
		//create the test class

		InputStream speechStream = this.synthesize(voiceText, OutputFormat.Mp3);

		//create an MP3 player
		AdvancedPlayer player = new AdvancedPlayer(speechStream,
				javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice());
		player.setPlayBackListener(new PlaybackListener() {
			@Override
			public void playbackStarted(PlaybackEvent evt) {
				System.out.println("Playback started");
				System.out.println(voiceText);
			}

			@Override
			public void playbackFinished(PlaybackEvent evt) {
				System.out.println("Playback finished");
			}
		});


		// play it!
		player.play();

	}
}