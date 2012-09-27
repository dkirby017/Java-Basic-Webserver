package server.servlet;

import server.io.IRequest;
import server.io.IResponse;

/**
 * Interface for simple servlet classes
 */
public interface ISimpleServlet {
	
	/**
	 * Specifies the servlet behaviour for a GET request
	 * 
	 * @param request the client's request
	 * @param response the response object for the client
	 */
	public void doGet(IRequest request, IResponse response);
		
	
	/**
	 * Specifies the servlet behaviour for a POST request
	 * 
	 * @param request the client's request
	 * @param response the response object for the client
	 */
	public void doPost(IRequest request, IResponse response);
	
}
