package gui;
import java.util.Calendar;

public class GoodMorning {

    public static String getDayTime( ) {
    	String timeString="";
        Calendar cal = Calendar.getInstance();
        int hour = cal.get( Calendar.HOUR_OF_DAY );
        int month = cal.get( Calendar.MONTH );

        if( hour == 5 ) {
            if( month > 2 && month < 9 ) {
                timeString= "Good morning!";
            }
            else {
            	timeString= "Good night!";
            }
        }
        else if( hour > 5 && hour < 12 ) {
        	timeString="Good morning!";
        }
        else if( hour > 11 && hour < 17 ) {
        	timeString="Good afternoon!";
        }
        else if( hour == 17 ) {
            if( month > 2 && month < 9 ) {
            	timeString= "Good afternoon!";
            }
            else {
            	timeString="Good evening!";
            }
        }
        else if( hour > 17 && hour < 19 ) {
        	timeString="Good evening!";
        }
        else {
        	timeString= "Good night!";
        }
        return timeString;
    }
}