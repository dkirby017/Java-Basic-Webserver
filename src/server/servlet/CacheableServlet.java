package server.servlet;

import server.memory.Cacheable;

/**
 * Cacheable wrapper for the ISimpleServlet interface
 * 
 * @author dkirby
 *
 */
public class CacheableServlet extends Cacheable {
	
	private ISimpleServlet _Servlet;
	
	/**
	 * Constructor
	 * 
	 * @param servlet the ISimpleServlet object to wrap
	 */
	public CacheableServlet(ISimpleServlet servlet) 
	{
		_Servlet = servlet;
		
		resetExpiry();
	}
	
	/**
	 * Gets the ISimpleServlet object
	 * 
	 * @return the ISimpleServlet object
	 */
	public ISimpleServlet get_Servlet() 
	{	
		resetExpiry();
		
		return _Servlet;
	}
}
