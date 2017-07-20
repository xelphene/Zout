
package net.xelphene.zout.hash;

import java.security.spec.*;
import java.security.*;

public class HashGenerator
{
	public static void main(String[] args) 
	{
		if( args.length!=1 ) {
			System.err.println("HashGenerator called with incorrect number of arguments. Exactly one argument (the plaintext password to be hashed) is expected");
			System.exit(1);
		}
		try {
			System.out.println(Hasher.genHash(args[0]));		
		} catch( NoSuchAlgorithmException nsa ) {
			System.out.println(nsa.toString());
			System.exit(1);
		} catch( InvalidKeySpecException iks ) {
			System.out.println(iks.toString());
			System.exit(1);
		}
		
		System.exit(0);
	}
}
