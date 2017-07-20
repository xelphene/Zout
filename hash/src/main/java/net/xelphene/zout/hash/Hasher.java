
package net.xelphene.zout.hash;

import java.util.*;
import java.security.spec.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.SecretKeyFactory;
import javax.xml.bind.DatatypeConverter;

public class Hasher 
{
	public static final int ITERATIONS = 65536;
	public static final int KEYLEN = 128;

	public static final byte[] salt = new byte[] {
		(byte) 122,
		(byte) 116,
		(byte) 212,
		(byte) 132,
		(byte) 187,
		(byte) 227,
		(byte) 79,
		(byte) 184,
		(byte) 150,
		(byte) 38,
		(byte) 175,
		(byte) 181,
		(byte) 173,
		(byte) 102,
		(byte) 34,
		(byte) 56
	};
	
	public static String genHash( String password )
	throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEYLEN);

		// There is no stronger hash algorithm available in here than SHA-1
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

		byte[] hash = factory.generateSecret(keySpec).getEncoded();
		
		String hexEncodedHash = byteArrayToHex(hash);
		return hexEncodedHash;
	}

	public static String byteArrayToHex(byte[] bytes) {
		char[] chars = new char[bytes.length*2];
		for( int i=0; i<bytes.length; i++ ) {
			char[] x = String.format("%2x", (int) bytes[i]).replace(" ","0").toCharArray();

			/* grabbing the last two characters in case this was a negative
			 * number in two's compliment */
			chars[i*2] = x[x.length-2];
			chars[i*2+1] = x[x.length-1];
			
		}
		return new String(chars);
	}
	
	public static byte[] hexToByteArray(String hex) {
		// TODO: validate hex parameter
		return DatatypeConverter.parseHexBinary(hex);
	}
}
