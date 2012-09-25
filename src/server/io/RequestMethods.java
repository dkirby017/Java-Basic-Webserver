package server.io;

/**
 * Contains the specifics for the supported request methods
 * 
 * @author dkirby
 *
 */
public class RequestMethods 
{
	/**
	 * Enumeration of supported request methods
	 * 
	 * @author dkirby
	 *
	 */
	public enum Method {GET, POST, INVALID};
	
	/**
	 * Maps a given request method string to the appropriate method enumeration value
	 * 
	 * @param method the method string to map
	 * @return the appropriate method enumeration value for the given string, or invalid if the string does not
	 * match a supported method
	 */
	static public Method mapMethod(String method)
	{
		if (method.equals("GET"))
		{
			return Method.GET;
		}
		else if (method.equals("POST"))
		{
			return Method.POST;
		}
		else 
		{
			return Method.INVALID;
		}
	}
}
