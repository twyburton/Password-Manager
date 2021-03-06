package twy.burton.core;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;

import twy.burton.security.T21Signature;

public class KeySetup {

	public static void main(String[] args) throws Exception {
		
		 KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		 generator.initialize(2048*2, new SecureRandom());
		 KeyPair pair = generator.generateKeyPair();

		 System.out.println(pair.getPublic());
		 System.out.println(pair.getPrivate());
		 
		 String message = "yolo. gg.";
		 
		 byte[] sig = T21Signature.sign( message.getBytes() , pair.getPrivate() );
		 
		 System.out.println(T21Signature.verify(message.getBytes(), sig, pair.getPublic()));
		 
	}

}
