package twy.burton.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class T21Signature {

	public static byte[] sign(byte[] plainText, PrivateKey privateKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
		Signature privateSignature = Signature.getInstance("SHA256withRSA");
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText);
		
		return privateSignature.sign();
	
	}
	
	public static boolean verify(byte[] plainText, byte[] signature, PublicKey publicKey) throws Exception {
		Signature publicSignature = Signature.getInstance("SHA256withRSA");
		publicSignature.initVerify(publicKey);
		publicSignature.update(plainText);
		
		return publicSignature.verify(signature);
	}
}
