
package net.xelphene.zout.servlet;

public class NoPasswordResourceException extends Exception {
	public String toString() {
		return "Password resource file /WEB-INF/password does not exist";
	}
}
