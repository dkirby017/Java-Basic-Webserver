package server.resource;

import server.memory.ObjectCache;

/**
 * A singleton subclass of the ObjectCache class. Caches static resources
 * 
 * @author dkirby
 *
 */
public class ResourceCache extends ObjectCache 
{
	static private ResourceCache _instance = null;
	
	private ResourceCache()
	{
		super();
	}
	
	/**
	 * Gets the singleton instance of the ResourceCache
	 * 
	 * @return the singleton ResourceCache instance
	 */
	static public ResourceCache getInstance()
	{
		if (_instance == null)
			_instance = new ResourceCache();
		
		return _instance;
	}
}
