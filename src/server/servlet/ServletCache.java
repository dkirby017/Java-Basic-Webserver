package server.servlet;

import server.memory.ObjectCache;

public class ServletCache extends ObjectCache 
{
	static private ServletCache _instance = null;
	
	private ServletCache()
	{
		super();
	}
	
	static public ServletCache getInstance()
	{
		if (_instance == null)
			_instance = new ServletCache();
		
		return _instance;
	}	
}
