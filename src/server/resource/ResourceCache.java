package server.resource;

import server.memory.ObjectCache;

public class ResourceCache extends ObjectCache 
{
	static private ResourceCache _instance = null;
	
	private ResourceCache()
	{
		super();
	}
	
	static public ResourceCache getInstance()
	{
		if (_instance == null)
			_instance = new ResourceCache();
		
		return _instance;
	}
}
