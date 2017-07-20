
package net.xelphene.zout.servlet;

public class InvalidHashException extends Exception {
	private String mLoadedHash;
	
	InvalidHashException( String loadedHash ) {
		mLoadedHash=loadedHash;
	}
	
	public String toString() {
		return "Password hash loaded from /WEB-INF/password is not a valid hash: "+mLoadedHash;
	}
}
