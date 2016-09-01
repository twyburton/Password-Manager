package twy.burton.utilities;

import twy.burton.userinterface.Style;

public class OutputConsole {

	public void print( String txt ){
		System.out.print( txt );
	}
	
	public void println( String txt ){
		System.out.println( txt );
	}
	
	public void clear(){
		print( Style.CLEAR );
	}
	
}
