
package net.xelphene.zout.servlet;

public class AuthenticationResult {
	public static final int INVALID_REQUEST=1;
	public static final int INTERNAL_ERROR=2;
	public static final int WRONG_PASSWORD=3;
	public static final int OK=4;
	public static final int NO_AUTHZ_HEADER=5;
	
	private int mResultCode;
	private String mReason;

	AuthenticationResult(int resultCode, String reason) {
		assert 
			resultCode==INVALID_REQUEST || 
			resultCode==INTERNAL_ERROR ||
			resultCode==WRONG_PASSWORD ||
			resultCode==OK ||
			resultCode==NO_AUTHZ_HEADER
			;
			
		mResultCode=resultCode;
		
		if( reason!=null ) {
			mReason=reason;
		} else {
			mReason="";
		}
	}

	AuthenticationResult(int resultCode ) {
		assert 
			resultCode==INVALID_REQUEST || 
			resultCode==INTERNAL_ERROR ||
			resultCode==WRONG_PASSWORD ||
			resultCode==OK ||
			resultCode==NO_AUTHZ_HEADER
			;
			
		mResultCode=resultCode;
		mReason="";
	}

	public int getResultCode() {
		return mResultCode;
	}
	
	public String describe() {
		switch(mResultCode) {
			case INVALID_REQUEST: return "Invalid request: "+mReason;
			case INTERNAL_ERROR: return "Internal error: "+mReason;
			case WRONG_PASSWORD: return "Incorrect password.";
			case OK: return "Success.";
			case NO_AUTHZ_HEADER: return "No Authorization header in request.";
			default: return "Unknown";
		}
	}
}
