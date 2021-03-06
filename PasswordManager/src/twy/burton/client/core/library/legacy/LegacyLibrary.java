package twy.burton.client.core.library.legacy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import twy.burton.client.core.service.Service;
import twy.burton.client.security.LegacyAES;
import twy.burton.utilities.FileAccess;
import twy.burton.utilities.Timestamp;

/**
 * This class is responsible for dealing with importing legacy library files
 * 
 * @author Thomas WY Burton
 *
 */
public class LegacyLibrary {

	private static String valid_identifier = "VALIDATE";
	public static String SALT = "XZxjdJoOtaygV15OJqWQYq#zQJUG0fmMfYe*#hWQABf$cTguZt";
	
	public static List<Service> readFile( String file_path , byte[] key, byte[] iv ) throws IOException{
		
		List<Service> list = new ArrayList<Service>();
		
		// === READ DATA STORAGE BYTE ARRAY FROM FILE ===
		FileInputStream fin = new FileInputStream( new File(file_path) );
		byte[] data = new byte[fin.available()];
		fin.read(data);
		fin.close();
		
		// === DECRYPT DATA STORAGE BYTE ARRAY ===
		try {
			data = LegacyAES.decrypt( data , key, iv);
		} catch ( Exception e ){
			System.out.println("DECRYPTION ERROR!");
			return null;
		}
		
		// === PROCESS DATA STORAGE BYTE ARRAY ===
		int current_index = 0; 
		
		// === Validate ===
		for( int i = 0 ; i < valid_identifier.length(); i++ ){
			if( valid_identifier.charAt(i) != data[current_index++]){
				System.out.println("INVALID DECRYPTION!");
				return null;
			}
		}
	
		// Get length of list
		byte[] length_list = new byte[4];
		for( int i = 0 ; i < 4; i++ )
			length_list[i] = data[current_index++];
		
		int length_list_int = FileAccess.bb_to_int_le( length_list );
		
		// Get list items and create password records
		
		for( int i = 0 ; i < length_list_int ; i++ ){
			
			// Service
			byte[] length_service = new byte[4];
			for( int j = 0 ; j < 4; j++ )
				length_service[j] = data[current_index++];
			
			int length_service_int = FileAccess.bb_to_int_le( length_service );
			String service = "";
			for( int j = 0 ; j < length_service_int; j++ )
				service += (char) data[current_index++];
			
			// Username
			byte[] length_username = new byte[4];
			for( int j = 0 ; j < 4; j++ )
				length_username[j] = data[current_index++];
			
			int length_username_int = FileAccess.bb_to_int_le( length_username );
			String username = "";
			for( int j = 0 ; j < length_username_int; j++ )
				username += (char) data[current_index++];
			
			// password
			byte[] length_password = new byte[4];
			for( int j = 0 ; j < 4; j++ )
				length_password[j] = data[current_index++];
			
			int length_password_int = FileAccess.bb_to_int_le( length_password );
			String password = "";
			for( int j = 0 ; j < length_password_int; j++ )
				password += (char) data[current_index++];
			
			
			Service ser = new Service();
			ser.setName(service);
			ser.setUsername(username);
			ser.setPassword(password);
			ser.addServiceExtra("LastUpdated", Timestamp.getTimestamp() );
			
			list.add( ser );
			
		}
			
		return list;
		
		
	}
	
	public static String hashPassword( String password ){
		
		// Salt password
		String salted = password + SALT;
		// Hash password
		return hashString( salted );
		
	}
	
	public static String hashString( String text){
	
		byte[] ret = text.getBytes(); 
		
		for( int i = 0 ; i < 10 ; i++ )
			ret = singleHash( ret );
		
		String result = "";
		for( int i = 0 ; i < ret.length ; i++ )	
			result += (char) ret[i];
		
		return result;
			
	}
	
	public static byte[] singleHash( byte[] input ){
		try {
			MessageDigest hash = MessageDigest.getInstance("SHA-256");
			hash.update( input );
			
			return hash.digest();
			
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
}
