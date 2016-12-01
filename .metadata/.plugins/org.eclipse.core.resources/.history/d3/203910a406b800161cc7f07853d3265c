package twy.burton.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	
	
	// Encryption and Decryption functions modified from https://gist.github.com/bricef/2436364
	public static byte[] encrypt(byte[] plainText, byte[] encryptionKey, byte[] iv) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		
		// = Generate padding for plain text =
		// Workout how much padding is required
		int plainTextLength = plainText.length;
		int paddingLength = 16 - (plainTextLength % 16);
		if( paddingLength == 0 ){
			paddingLength += 16;
		}
		
		// Add padding to plain text
		byte[] paddedPlainText = new byte[ plainTextLength + paddingLength];
		
		for( int i = 0 ; i < paddedPlainText.length ; i++ ){
			if( i < plainTextLength){
				paddedPlainText[i] = plainText[i];
			} else {
				paddedPlainText[i] = (byte) paddingLength;
			}
		}
		
		// = Encrypt =
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
		SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
		//SecretKey key = new SecretKeySpec( encryptionKey , 0, encryptionKey.length, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec( iv ));
		return cipher.doFinal( paddedPlainText );
	}
	
	public static byte[] decrypt(byte[] cipherText, byte[] encryptionKey, byte[] iv) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		
		
		
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
		SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
		//SecretKey key = new SecretKeySpec( encryptionKey , 0, encryptionKey.length, "AES");
		cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec( iv ));	
		byte[] plainText = cipher.doFinal(cipherText);
		
		// = Remove Padding =
		// Get padding value from last array element
		int paddingValue = plainText[ plainText.length - 1 ];
		
		// Copy to new array without padding
		byte[] originalText = new byte[plainText.length - paddingValue];
		for( int i = 0 ; i < originalText.length ; i++ ){
			originalText[i] = plainText[i];
		}
		
		return originalText;
	}
}
