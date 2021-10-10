package com.selenium.automation;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeAuto {
	static WebDriver driver;
public ChromeAuto() {

}


public static void main(String []args){
	System.setProperty("webdriver.chrome.driver", "E:\\eclipse WorkSpace\\selenium chrome\\chromedriver_win32\\chromedriver.exe");
	ChromeOptions options = new ChromeOptions();
	options.addArguments("user-data-dir=C:/Users/dheer/AppData/Local/Google/Chrome/User Data");
	options.addArguments("--start-maximized");


driver= new ChromeDriver(options);

	openGmail();
driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
createMail();


}

public static void sendmail(){
	System.setProperty("webdriver.chrome.driver", "E:\\eclipse WorkSpace\\selenium chrome\\chromedriver_win32\\chromedriver.exe");
	ChromeOptions options = new ChromeOptions();
	options.addArguments("user-data-dir=C:/Users/dheer/AppData/Local/Google/Chrome/User Data");
	options.addArguments("--start-maximized");


driver= new ChromeDriver(options);

openGmail();
driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
createMail();


}
public static void openChrome(){
	System.setProperty("webdriver.chrome.driver", "E:\\eclipse WorkSpace\\selenium chrome\\chromedriver_win32\\chromedriver.exe");
	driver= new ChromeDriver();
	driver.get("https://www.google.com");
}


public static void openGmail() {
	driver.get("https://mail.google.com/mail/u/0/#inbox");
}
public static void createMail() {
	//*[@id=":k9"]/div/div
	driver.findElement(By.xpath("//*[@id=':k1']/div/div")).click();

	//entering recieptent name
	driver.findElement(By.name("to")).click();
	driver.findElement(By.name("to")).sendKeys("dheerajkhushalani2000@gmail.com");


	driver.findElement(By.cssSelector("div[aria-label= 'Message Body']")).click();

	//entering subject for email.
	driver.findElement(By.name("subjectbox")).click();

	driver.findElement(By.name("subjectbox")).sendKeys("This is a automatic mail");

	//entering message for you sir.
	driver.findElement(By.cssSelector("div[aria-label= 'Message Body']")).click();
	driver.findElement(By.cssSelector("div[aria-label= 'Message Body']")).sendKeys("This is a new automatic messages");




}
}
