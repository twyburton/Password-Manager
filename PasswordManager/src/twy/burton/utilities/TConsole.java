package twy.burton.utilities;

import java.io.Console;
import java.util.Scanner;

public class TConsole {
	
	private static TConsole con = null;
	
	private static TConsole getCon(){
		if( con == null ){
			con = new TConsole();
		}
		return con;
	}
	
	
	private Scanner scan;
	private Console console;
	
	public Scanner getScanner(){
		return scan;
	}
	
	public Console getConsole(){
		return console;
	}
	
	
	
	public TConsole( ){
		scan = new Scanner( System.in );
		console = System.console();
	}
	
	public TConsole( Scanner scan ){
		this.scan = scan;
	}
	
	
	
	

	public static void print( String txt ){
		System.out.print( txt );
	}
	
	public static void println( String txt ){
		System.out.println( txt );
	}
	
	public static void println( String txt , String style ){
		System.out.println( style + txt + Style.RESET );
	}
	
	public static void clear(){
		print( Style.CLEAR );
	}
	
	public static String getInput( String prompt ){
		print(prompt);
		String input = TConsole.getCon().getScanner().nextLine();
		return input;
	}
	
	public static String[] getSeperatedInput( String prompt ){
		print(prompt);
		String input = TConsole.getCon().getScanner().nextLine();
		return input.split(" ");
	}
	
	public static String getSecurePassword( String prompt ){
		 String password = new String(TConsole.getCon().getConsole().readPassword( Style.YELLOW + prompt + " > " + Style.WHITE ));
		 return password;
	}
	
}
