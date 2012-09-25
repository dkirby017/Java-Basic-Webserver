package server.io;

import server.io.RequestMethods.Method;

/**
 * Interface for http request objects
 * 
 * @author dkirby
 *
 */
public interface IRequest {

	/**
	 * Gets the Host parameter as supplied by the client
	 * 
	 * @return The host 
	 */
	public String getHost();
	
	/**
	 * Gets the entire url path as supplied by the client, including request parameters
	 * 
	 * @return The host 
	 */
	public String getUrlPath();

	/**
	 * Gets a specific parameter as parsed from request url
	 * 
	 * @param name The parameter name
	 * @return The parameter value
	 */
	public String getUrlParameter(String name);
	
	/**
	 * Gets a request header as supplied by the client
	 * 
	 * @param name The header name
	 * @return The header value
	 */
	public String getHeader(String name);
	
	/**
	 * Gets the client's network address
	 * 
	 * @return The clients network address
	 */
	public java.net.InetAddress getClientAddress();
	
	/**
	 * Gets the request method as supplied by the client
	 * 
	 * @return The request method
	 */
	public Method getMethod();
	
	/**
	 * Gets the specified parameter as parsed from the request POST data
	 * 
	 * @return the POST parameter value
	 */
	public String getPostParameter(String name);
}
