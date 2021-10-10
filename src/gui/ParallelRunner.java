package gui;

import java.io.IOException;

import com.amazonaws.polly.PollyDemo;
import com.googlesearch.GoogleSearchResult;
import com.media.youtubePlayer.VideoPlayer;

public class ParallelRunner {

	PollyDemo pollyDemo;
	GoogleSearchResult googleSearchResult;

	public ParallelRunner() {
		pollyDemo = new PollyDemo();
		googleSearchResult =new GoogleSearchResult();
	}
	public static void main(String []args) throws IOException {
	ParallelRunner pRunner= new ParallelRunner();
//		pRunner.startSearching("Narendra modi");
		pRunner.playMovie();
		
	}

	void startSearching(String quString) throws IOException {
		System.out.println("Command: Searching for"+ quString);
		try {
		pollyDemo.mainPlay("Searching on web for "+ quString);

		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

		try {
			pollyDemo.mainPlay("According to latest internet data "+googleSearchResult.getSearchResult(quString));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startBrowser() throws IOException {

		System.out.println("Command: Open Browser");
		try {
		pollyDemo.mainPlay("Opening google chrome");
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

	Runtime.getRuntime().exec(new String[]{"cmd", "/c","start chrome "});
	}
	public void startNotepad() throws IOException {

		System.out.println("Command: Open notepad");
		try {
		pollyDemo.mainPlay("Opening notepad for you sir.");
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

	Runtime.getRuntime().exec(new String[]{"cmd", "/c","start notepad "});
	}

	public void helloCommand() {
		try {
			pollyDemo.mainPlay("Hello Sir, I am Maya your personal AI assistant.");
			pollyDemo.mainPlay("I am a new generation voice assistant better than google and siri. haha haha");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	public void startSearchingFor(String resultStr) {
		System.out.println("Command: Searching for"+ resultStr);
		try {
		pollyDemo.mainPlay("I am Searching on internet "+ resultStr);

		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

	try {
		String resultString=googleSearchResult.getSearchResult(resultStr);
		pollyDemo.mainPlay("According to internet results "+resultString);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	public void playMovie() {
		System.out.println("Command: Playing movie on youtube");
		try {
		pollyDemo.mainPlay("Finding top movie collections of 2021");

		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

	}
	
}
