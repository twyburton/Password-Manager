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
		
		UserInterface ui = new UserInterface();
		ui.firstSetup();
		ui.run();

	}

}
