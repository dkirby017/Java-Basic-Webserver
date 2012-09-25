package server.servlet;

import java.io.IOException;

import server.io.ClientConnection;
import server.io.IRequest;
import server.io.MalformedRequestException;
import server.io.RequestMethods.Method;
import server.io.UnsupportedMethodException;
import server.processor.IProcessor;
import servlet.ISimpleServlet;

public class ServletProcessor implements IProcessor {

	@Override
	public void process(ClientConnection connection) throws ClassNotFoundException, IOException, MalformedRequestException, UnsupportedMethodException, InstantiationException, IllegalAccessException 
	{
		IRequest request = connection.getRequest();
		
		String url = request.getUrlPath();
		
		// Remove url parameters (if any)
		int index = url.indexOf("?");
		if (index != -1)
			url = url.substring(0, index);
		
		ServletManager manager = ServletManager.getInstance();
		
		ISimpleServlet servlet = manager.getServlet(url);
		
		Method method = request.getMethod();
		
		// invalid method would have been caught during the request parsing,
		// so don't need to check for it here
		if (method == Method.GET)
			servlet.doGet(request, connection.getResponse());
		
		else if (method == Method.POST)
			servlet.doPost(request, connection.getResponse());
		
	}

}
