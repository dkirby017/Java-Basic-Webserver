package server.servlet;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import server.io.ClientConnection;
import server.io.IRequest;
import server.io.MalformedRequestException;
import server.io.RequestMethods.Method;
import server.io.UnsupportedMethodException;
import server.processor.IProcessor;
import servlet.ISimpleServlet;

/**
 * Processes the servlet request and passes control off to the servlet object. Any exceptions prior to passing the control
 * off to the servlet are passed up the call stack.
 * 
 * @author dkirby
 *
 */
public class ServletProcessor implements IProcessor {

	@Override
	public void process(ClientConnection connection) throws ClassNotFoundException, IOException, MalformedRequestException, UnsupportedMethodException, InstantiationException, IllegalAccessException, ParserConfigurationException, SAXException 
	{
		IRequest request = connection.getRequest();
		
		String url = request.getUrlPath();
		
		// Remove url parameters (if any)
		int index = url.indexOf("?");
		if (index != -1)
			url = url.substring(0, index);
		
		// get the servlet manager
		ServletManager manager = getServletManager();
		
		// get the servlet
		ISimpleServlet servlet = manager.getServlet(url);
		
		// get the request method
		Method method = request.getMethod();
		
		// invalid method would have been caught during the request parsing,
		// so don't need to check for it here
		if (method == Method.GET)
			servlet.doGet(request, connection.getResponse());
		
		else if (method == Method.POST)
			servlet.doPost(request, connection.getResponse());
		
	}
	
	/**
	 * Gets the Servlet Manager
	 * 
	 * @return the Servlet Manager
	 */
	ServletManager getServletManager()
	{
		return ServletManager.getInstance();
	}

}
