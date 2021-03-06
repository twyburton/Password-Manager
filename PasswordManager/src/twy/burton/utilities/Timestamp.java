package twy.burton.utilities;

import java.util.Calendar;

public class Timestamp {

	public static String getTimestamp(){
		Calendar cal = Calendar.getInstance();
		return String.format("%02d",cal.get(Calendar.HOUR_OF_DAY))
				+ ":" + String.format("%02d",cal.get(Calendar.MINUTE)) 
				+ ":" + String.format("%02d",cal.get(Calendar.SECOND)) + " "
				+ String.format("%02d",cal.get(Calendar.DATE)) 
				+ "/" + String.format("%02d",(cal.get(Calendar.MONTH)+1)) 
				+ "/" + cal.get(Calendar.YEAR);
	}

}
