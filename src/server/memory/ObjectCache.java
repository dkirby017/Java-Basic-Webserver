package server.memory;

import java.util.HashMap;

public class ObjectCache 
{	
	private HashMap<String, Object> _cache;
	
	public ObjectCache() {
		_cache = new HashMap<>();	
	}
	
	synchronized public void cache(String label, Object object)
	{		
		_cache.put(label, object);
	}
	
	synchronized public Object get(String label)
	{
		return _cache.get(label);
	}	
}
