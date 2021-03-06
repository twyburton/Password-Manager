package twy.burton.client.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

import twy.burton.client.core.Constants;

public class TextCrypt {

	public static String encrypt( String txt, String password ){
		
		
		try {
			byte[] key = PMAES.passwordToKey(password, Constants.NUMBER_OF_TIMES_TO_HASH_PASSWORD);
			byte[] ciphertext = AES.encrypt(txt.getBytes(), key);
	
			return DatatypeConverter.printHexBinary(ciphertext);
			
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			return "ERROR WITH ENCRYPTION";
		}
		
		
	}
	
	public static String decrypt( String cipher, String password ){
		
		try {
			byte[] key = PMAES.passwordToKey(password, Constants.NUMBER_OF_TIMES_TO_HASH_PASSWORD);
			byte[] decodedHex = DatatypeConverter.parseHexBinary(cipher);			
			byte[] txt = AES.decrypt(decodedHex, key);
			
			String plain = "";
			for( int i = 0 ; i < txt.length; i++ ) plain += (char)txt[i];

			return plain;
		} catch (Exception e ){
			e.printStackTrace();
			return "ERROR WITH DECRYPTION";
		}
	}
	
}
