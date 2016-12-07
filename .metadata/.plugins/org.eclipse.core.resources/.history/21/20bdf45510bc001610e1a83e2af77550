package twy.burton.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import twy.burton.core.Constants;
import twy.burton.utilities.ArrayUtils;

public class AES {
	
	// Encryption and Decryption functions modified from https://gist.github.com/bricef/2436364
	/**
	 * Encrypt a byte array
	 * @param plainText The plaintext byte array
	 * @param encryptionKey The AES encryption key as a byte array
	 * @return The encrypted data in a byte array
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] encrypt(byte[] plainText, byte[] encryptionKey) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		// Create Initial Vector
		SecureRandom random = new SecureRandom();
		byte[] iv = new byte[Constants.ENCRYPTION_INITIAL_VECTOR_LENGTH];
		random.nextBytes(iv);
		
		// Add random pre data
		SecureRandom ran = new SecureRandom();
		byte[]  randomPreData = new byte[Constants.LENGTH_OF_RANDOM_BEFORE_ENCRYPTION]; 
		ran.nextBytes( randomPreData);
		
		// Encrypt data
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec( iv ));
		byte[] cipherText = cipher.doFinal( ArrayUtils.concat( randomPreData, plainText) );
		
		// Concatenate IV and cipher text
		return ArrayUtils.concat(iv, cipherText);
		
	}
	
	/**
	 * Decrypt a byte array
	 * @param cipherText The cipher text byte array
	 * @param encryptionKey The AES encryption key as a byte array
	 * @return The plain text data in a byte array
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] decrypt(byte[] cipherText, byte[] encryptionKey ) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		
		// Get IV from cipher text
		byte[] iv = new byte[Constants.ENCRYPTION_INITIAL_VECTOR_LENGTH];
		byte[] data = new byte[ cipherText.length - iv.length ];
		
		for( int i = 0 ; i < iv.length; i++ ) iv[i] = cipherText[i];
		for( int i = 0 ; i < data.length; i++ ) data[i] = cipherText[i + iv.length ];
		
		// Decrypt Data
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
		cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec( iv ));	
		byte[] plain =  cipher.doFinal(data);
		
		// Remove random pre data
		byte[] plainWithoutRandom = new byte[plain.length - Constants.LENGTH_OF_RANDOM_BEFORE_ENCRYPTION];
		for( int i = 0 ; i < plainWithoutRandom.length; i++ )
			plainWithoutRandom[i] = plain[i + Constants.LENGTH_OF_RANDOM_BEFORE_ENCRYPTION ];
		return plainWithoutRandom;
		
	}
}
