package server.memory;

import java.util.HashMap;

/**
 * Implements a synchronized cache of Objects
 * 
 * @author dkirby
 *
 */
public class ObjectCache 
{	
	protected HashMap<String, Object> _cache;
	
	public ObjectCache() {
		_cache = new HashMap<>();	
	}
	
	/**
	 * Caches the input object
	 * 
	 * @param label a string to identify the object
	 * @param object the object to cache
	 */
	synchronized public void cache(String label, Object object)
	{		
		_cache.put(label, object);
	}
	
	/**
	 * Gets a cached object
	 * 
	 * @param label the identifying string for the object
	 * @return the cached object, null if it doesn't exist
	 */
	synchronized public Object get(String label)
	{
		return _cache.get(label);
	}	
}
