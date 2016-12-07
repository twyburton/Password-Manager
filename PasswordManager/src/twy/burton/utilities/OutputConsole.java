package twy.burton.utilities;

import java.io.Console;
import java.util.Scanner;

public class OutputConsole {
	
	Scanner scan;
	Console console = System.console();
	
	public OutputConsole( Scanner scan ){
		this.scan = scan;
	}

	public void print( String txt ){
		System.out.print( txt );
	}
	
	public void println( String txt ){
		System.out.println( txt );
	}
	
	public void println( String txt , String style ){
		System.out.println( style + txt + Style.RESET );
	}
	
	public void clear(){
		print( Style.CLEAR );
	}
	
	public String getInput( String prompt ){
		print(prompt);
		String input = scan.nextLine();
		return input;
	}
	
	public String[] getSeperatedInput( String prompt ){
		print(prompt);
		String input = scan.nextLine();
		return input.split(" ");
	}
	
	public String getSecurePassword( String prompt ){
		 String password = new String(console.readPassword( Style.YELLOW + prompt + " > " + Style.WHITE ));
		 return password;
	}
	
}
