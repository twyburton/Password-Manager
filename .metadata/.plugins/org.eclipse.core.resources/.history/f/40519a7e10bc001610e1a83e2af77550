package twy.burton.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class LegacyAES {
	
	public static byte[] decrypt( byte[] cipherText , byte[] encryptionKey,  byte[] iv ){
		// = Decrypt =
		byte[] paddedOriginalText;
		try {
			paddedOriginalText = decryptAX( cipherText, encryptionKey, iv );
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		// = Remove Padding =
		// Get padding value from last array element
		int paddingValue = paddedOriginalText[ paddedOriginalText.length - 1 ]; 
		
		// Copy to new array without padding
		byte[] originalText = new byte[paddedOriginalText.length - paddingValue];
		for( int i = 0 ; i < originalText.length ; i++ ){
			originalText[i] = paddedOriginalText[i];
		}
		
		// = Remove randomShit =
		byte[] data = new byte[ originalText.length - 16 ];
		for( int i = 0 ; i < data.length; i++ ){
			data[i] = originalText[ i + 16 ];
		}
		
		// = Return Original Text =
		return data;
	}
	
	
	
	// Encryption and Decryption functions modified from https://gist.github.com/bricef/2436364

	// This function is used as an auxiliary function to decrypt the data
	private static byte[] decryptAX( byte[] cipherText, byte[] encryptionKey, byte[] iv ) throws Exception{
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
		SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
		//SecretKey key = new SecretKeySpec( encryptionKey , 0, encryptionKey.length, "AES");
		cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec( iv ));	
		return cipher.doFinal(cipherText);
	}
}
