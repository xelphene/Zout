
package net.xelphene.zout.servlet;

import javax.servlet.ServletContext;
import net.xelphene.zout.hash.Hasher;
import java.io.InputStream;
import java.util.Arrays;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordManager {
	private String loadedPasswordHash;

	private void loadPasswordHash( ServletContext servletContext ) 
	throws NoPasswordResourceException, IOException, InvalidHashException
	{
		InputStream passwordFile = servletContext.getResourceAsStream("/WEB-INF/password");
		if( passwordFile==null ) {
			throw new NoPasswordResourceException();
		}
		
		String passwordHash;
		byte[] buffer = new byte[128];
		int len = passwordFile.read(buffer);
		passwordHash = new String( Arrays.copyOfRange(buffer,0,len) );
		
		if( ! passwordHash.matches("^[A-Za-z0-9]+$") ) {
			throw new InvalidHashException(passwordHash);
		}
		
		loadedPasswordHash = passwordHash;
	}
	
	public boolean verifyPassword( ServletContext servletContext, String trialPassword ) 
	throws NoPasswordResourceException, IOException, InvalidHashException, 
		NoSuchAlgorithmException, InvalidKeySpecException
	{
		if( loadedPasswordHash==null ) {
			loadPasswordHash(servletContext);
		}
		String trialHash = Hasher.genHash(trialPassword);
		
		return trialHash.equals(loadedPasswordHash);
	}
}
