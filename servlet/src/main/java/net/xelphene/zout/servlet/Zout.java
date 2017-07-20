
package net.xelphene.zout.servlet;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.*;
import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import net.xelphene.zout.hash.Hasher;

// this will generate a warning because it isn't public, but its the easiest way
// right now
import sun.misc.BASE64Decoder; 

public class Zout extends HttpServlet {

	private Authenticator authenticator;

	/*
	Zout() {
	}
	*/

	public void init() throws ServletException {
		authenticator = new Authenticator();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException 
	{
		AuthenticationResult authResult = authenticator.authenticateRequest(getServletContext(), req);
		//authResult = new AuthenticationResult(AuthenticationResult.INTERNAL_ERROR, "blah blah");
		//authResult = new AuthenticationResult(AuthenticationResult.INVALID_REQUEST, "blah blah");
		
		if(
			authResult.getResultCode()==AuthenticationResult.NO_AUTHZ_HEADER ||
			authResult.getResultCode()==AuthenticationResult.WRONG_PASSWORD
		)
		{
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.setHeader("WWW-Authenticate", "Basic realm=\"\"");
			resp.setContentType("text/plain");
			PrintWriter out = resp.getWriter();
			out.println(authResult.describe());
		} else if( authResult.getResultCode()==AuthenticationResult.OK ) {
			doGetAuthed(req, resp);
		} else if( authResult.getResultCode()==AuthenticationResult.INTERNAL_ERROR ) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.setContentType("text/plain");
			PrintWriter out = resp.getWriter();
			out.println(authResult.describe());
		} else if( authResult.getResultCode()==AuthenticationResult.INVALID_REQUEST ) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.setContentType("text/plain");
			PrintWriter out = resp.getWriter();
			out.println(authResult.describe());
		} else {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			resp.setContentType("text/plain");
			PrintWriter out = resp.getWriter();
			out.println("AuthenticationResult has unknown getResultCode() value: "+authResult.getResultCode());
		}
	}
	
	public void doGetAuthed(HttpServletRequest req, HttpServletResponse resp) 
	throws ServletException, IOException
	{
		
		resp.setContentType("text/plain");

		PrintWriter out = resp.getWriter();
		out.println("hello from Zout.");
		
		ArrayList<String> argv;
		
		if( req.getParameter("argv")!=null ) {
			argv = parseSingularArgv( req.getParameter("argv") );
		} else {
			argv = parseSplitArgv( req );
		}
		
		out.println("--- uri ---");
		out.println(req.getRequestURI());

		out.println("--- qs ---");
		out.println(req.getQueryString());
		
		out.println("--- argv ---");
		for( String a : argv ) {
			out.println(a);
		}
	}
	
	public ArrayList<String> parseSingularArgv( String param ) {
		ArrayList<String> argv = new ArrayList<>();
		for( String a : param.split(" +") ) {
			argv.add(a);
		}
		return argv;
	}
	
	public ArrayList<String> parseSplitArgv( HttpServletRequest req ) {
		ArrayList<String> argv = new ArrayList<>();
		HashMap<Integer, String> argvMap = new HashMap<>();
		
		Pattern paramPattern = Pattern.compile("^argv([0-9]+)$");
		int argvMax=-1;
		for(Enumeration<String> e = req.getParameterNames(); e.hasMoreElements(); ) {
			String pName = e.nextElement();
			Matcher m = paramPattern.matcher(pName);
			if( m.matches() && m.group(1)!=null ) {
				int argvNum = Integer.parseInt(m.group(1));
				if( argvNum > argvMax ) {
					argvMax=argvNum;
				}
				argvMap.put(new Integer(argvNum), req.getParameter(pName));
			}
		}
		
		for( int i=0; i<=argvMax; i++ ) {
			if( argvMap.containsKey(i) ) {
				argv.add( argvMap.get(i) );
			} else {
				argv.add("");
			}
		}
		
		return argv;
	}

}
