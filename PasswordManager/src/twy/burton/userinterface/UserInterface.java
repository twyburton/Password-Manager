package twy.burton.userinterface;

import java.util.Scanner;

import twy.burton.core.Constants;
import twy.burton.utilities.OutputConsole;

public class UserInterface {

	private Scanner scanner = new Scanner(System.in);
	private boolean running = true;
	OutputConsole console = new OutputConsole();
	
	public UserInterface(){
		console.clear();
		console.println( Style.STYLE_UNDERLINE_ON + Constants.PROGRAM_NAME + " " + Constants.PROGRAM_VERSION + Style.STYLE_UNDERLINE_OFF );
	}
	
	// Perform first time setup. This includes creating directories and blank library files.
	public void firstSetup(){
		
	}
	
	public void run(){
		
		while( running ){
			
			console.print("> ");
			String userinput = scanner.nextLine();
			String[] input = userinput.split(" ");
			
			if( input[0].equals( "exit" ) || input[0].equals("quit") ){
				running = false;
			}
			
		}
		
		scanner.close();
		console.clear();
		
	}
	

	
}