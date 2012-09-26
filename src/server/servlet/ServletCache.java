package server.servlet;

import server.memory.ObjectCache;

/**
 * A singleton subclass of the ObjectCache class. Caches servlets
 * 
 * @author dkirby
 *
 */
public class ServletCache extends ObjectCache 
{
	static private ServletCache _instance = null;
	
	private ServletCache()
	{
		super();
	}
	
	/**
	 * Gets the singleton instance of the ServletCache
	 * 
	 * @return the singleton ServletCache instance
	 */
	static public ServletCache getInstance()
	{
		if (_instance == null)
			_instance = new ServletCache();
		
		return _instance;
	}	
}
