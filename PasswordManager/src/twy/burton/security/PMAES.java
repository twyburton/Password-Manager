package twy.burton.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import twy.burton.core.Constants;

public class PMAES extends AES{

	
	public static boolean writeEncryptedFile( String filename, String password , byte[] data){
		// Get key from password
		byte[] key = passwordToKey( password );
		
		try {
			// Encrypt data
			data = encrypt( data , key , Constants.ENCRYPTION_INITIAL_VECTOR);
			
			// Write encrypted data to file
			File file = new File( filename );
			FileOutputStream out = new FileOutputStream( file );
			out.write( data );
			out.close();
			
		} catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return false;
		} 
		
		return true;
	}
	
	public static byte[] readEncryptedFile( String filename, String password ){
		// Get key from password
		byte[] key = passwordToKey( password );
		
		
		try {
			// Read in file
			File file = new File( filename );
			FileInputStream in = new FileInputStream( file );
			int size = in.available();
			byte[] data = new byte[size];
			in.read(data, 0, size);
			in.close();
			
			// Decrypt file
			data = decrypt( data , key , Constants.ENCRYPTION_INITIAL_VECTOR );
			return data;
		} catch ( InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	// Convert the password into the encryption key
	private static byte[] passwordToKey( String password ){
		password = Constants.HASHING_SALT + password;
		byte[] hash = singleHash( password.getBytes() );	
		for( int i = 0 ; i < Constants.NUMBER_OF_TIMES_TO_HASH_PASSWORD; i++ ){
			hash = singleHash( hash );
		}
		return hash;
	}
	
	// Perform a SHA-256 hash
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