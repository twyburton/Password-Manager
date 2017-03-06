package twy.burton.client.main;

import twy.burton.client.userinterface.UserInterface;

public class Main {

	/*			
	 * 			--- System Terminology ---
	 * 
	 * 			Password Manager > Password Library > Service
	 *  
	 *  			A user can have several password Libraries. These can be stored on a remote server
	 *  		or on the users own computer. Each password library contains many services. A service 
	 *  		consists of a Name, Username, Password and possibly password extras.
	 * 
	 * */
	
	
	public static void main(String[] args) {
		
		// Flags
		boolean updated = false;
		
		// Get arguments
		for( int i = 0 ; i < args.length; i++ ){
			
			if( args[i].equals("-updated")){
				updated = true;
			}
			
		}
		
		// Set up the interface
		UserInterface ui = new UserInterface();
		ui.firstSetup();
		
		// Print any message to console
		if( updated ){
			ui.message("Software Updated!");
		}
		
		// Run user interface
		ui.run();

	}

}
