package twy.burton.client.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import twy.burton.client.core.Constants;

public class PMAES extends AES{

	
	public static boolean writeEncryptedFile( String filename, String password , byte[] data) throws InvalidKeyException{
		// Get key from password
		byte[] key = passwordToKey( password, Constants.NUMBER_OF_TIMES_TO_HASH_PASSWORD );
		
		try {
			// Encrypt data
			data = encrypt( data , key );
			
			// Write encrypted data to file
			File file = new File( filename );
			FileOutputStream out = new FileOutputStream( file );
			out.write( data );
			out.close();
			
		} catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return false;
		} 
		
		return true;
	}
	
	public static byte[] readEncryptedFile( String filename, String password ) throws BadPaddingException, InvalidKeyException{
		// Get key from password
		byte[] key = passwordToKey( password, Constants.NUMBER_OF_TIMES_TO_HASH_PASSWORD );
		
		
		try {
			// Read in file
			File file = new File( filename );
			FileInputStream in = new FileInputStream( file );
			int size = in.available();
			byte[] data = new byte[size];
			in.read(data, 0, size);
			in.close();
			
			// Decrypt file
			data = decrypt( data , key );
			return data;
		} catch ( NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Convert the password into the encryption key. This involves hashing the password many times.
	 * @param password The password string
	 * @param numberTimesToHash The number of times to hash the password
	 * @return The encryption key in a byte array
	 */
	public static byte[] passwordToKey( String password, int numberTimesToHash ){
		byte[] hash = singleHash( password.getBytes() );
		for( int i = 0 ; i < numberTimesToHash; i++ ){
			hash = singleHash( hash );
		}
		return hash;
	}
	
	/**
	 * Perform a SHA-256 hash
	 * @param input The byte array of data to hash
	 * @return A byte array of the hashed data
	 */
	private static byte[] singleHash( byte[] input ){
		try {
			MessageDigest hash = MessageDigest.getInstance("SHA-256");
			hash.update( input );
			return hash.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
}
