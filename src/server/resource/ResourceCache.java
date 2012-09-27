package server.resource;

import java.util.Timer;
import java.util.TimerTask;

import server.memory.ObjectCache;

/**
 * A singleton subclass of the ObjectCache class. Caches static resources
 * 
 * @author dkirby
 *
 */
public class ResourceCache extends ObjectCache 
{
	static private final int _cleanFrequency = 20 * 60 * 1000; // frequency to clean the cache (in ms)
	
	static private ResourceCache _instance = null;
	
	private ResourceCache()
	{
		super();
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				clean();
			}
		}, _cleanFrequency, _cleanFrequency);
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
