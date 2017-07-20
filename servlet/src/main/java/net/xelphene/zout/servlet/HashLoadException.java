
package net.xelphene.zout.servlet;

public class HashLoadException 
extends Exception
{
	private String mReason;
	
	public HashLoadException( String reason ) {
		mReason = reason;
	}
	
	public String toString() {
		return mReason;
	}
}
