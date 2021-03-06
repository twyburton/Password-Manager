package twy.burton.utilities;

import java.security.SecureRandom;

import twy.burton.client.core.Constants;

public class PasswordGenerator {

	public static String generateRandomPassword( int length ){
		
		String password = "";
		SecureRandom rand = new SecureRandom();
		for( int i = 0 ; i < length ; i ++ ){
		
				// nextInt is normally exclusive of the top value,
				// so add 1 to make it inclusive
				int randomNum = rand.nextInt((Constants.PASSWORD_CHARACTERS.length-1 - 0) + 1) + 0;
			    password += Constants.PASSWORD_CHARACTERS[ randomNum ];
			
		}	
		return password;
	}
	
}
