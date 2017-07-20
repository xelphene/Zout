
package net.xelphene.zout.servlet;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.io.IOException;

// this will generate a warning because it isn't public, but its the easiest way
// right now
import sun.misc.BASE64Decoder;


public class Authenticator {
	private PasswordManager passwordManager;

	Authenticator() {
		passwordManager = new PasswordManager();
	}

	public AuthenticationResult authenticateRequest(  ServletContext servletContext, HttpServletRequest request ) {
		String authzHeader = request.getHeader("Authorization");

		if( authzHeader==null ) {
			return new AuthenticationResult(AuthenticationResult.NO_AUTHZ_HEADER);
		}
		
		if( ! authzHeader.matches("^\\s*[Bb][Aa][Ss][Ii][Cc]\\s+.*") ) {
			return new AuthenticationResult(AuthenticationResult.INVALID_REQUEST, "Unknown HTTP Authorization method.");
		}

		Pattern p = Pattern.compile("^\\s*[Bb][Aa][Ss][Ii][Cc]\\s+([+/=A-Za-z0-9]+)$");
		Matcher m = p.matcher(authzHeader);
		if( ! m.matches() ) {
			return new AuthenticationResult(AuthenticationResult.INVALID_REQUEST, "Invalid Basic Authorization data.");
		}
		String authzData = m.group(1);
		String decodedAuthzData;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			decodedAuthzData = new String(decoder.decodeBuffer(authzData));
		} catch( IOException ioe ) {
			return new AuthenticationResult(AuthenticationResult.INVALID_REQUEST, "Incorrect Basic Authorization data Base64 encoding.");
		}
		
		String password;
		String[] creds = decodedAuthzData.split(":",2);
		if( creds.length==2 ) {
			password = creds[1];
		} else {
			return new AuthenticationResult(AuthenticationResult.INVALID_REQUEST, "Incorrect Basic Authorization data (wrong number of :-split parts).");
		}

		return authenticatePassword( servletContext, password );
	}

	public AuthenticationResult authenticatePassword( ServletContext servletContext, String password ) {
		
		try {
			if( passwordManager.verifyPassword( servletContext, password ) ) {
				return new AuthenticationResult(AuthenticationResult.OK, null);
			} else {
				return new AuthenticationResult(AuthenticationResult.WRONG_PASSWORD, null);
			}
		} catch( IOException e ) {
			return new AuthenticationResult(
				AuthenticationResult.INTERNAL_ERROR,
				"I/O error loading password resource file: "+e.toString()
			);
		} catch( NoSuchAlgorithmException e ) {
			return new AuthenticationResult(
				AuthenticationResult.INTERNAL_ERROR,
				"Java library does not support the required crypto primitives: "+e.toString()
			);
		} catch( InvalidKeySpecException e ) {
			return new AuthenticationResult(
				AuthenticationResult.INTERNAL_ERROR,
				"Java library does not support the required key length: "+e.toString()
			);
		} catch( NoPasswordResourceException e ) {
			return new AuthenticationResult(
				AuthenticationResult.INTERNAL_ERROR,
				e.toString()
			);
		} catch( InvalidHashException e ) {
			return new AuthenticationResult(
				AuthenticationResult.INTERNAL_ERROR,
				e.toString()
			);
		}
	}
}
