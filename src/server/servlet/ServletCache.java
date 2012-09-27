package server.servlet;

import java.util.Timer;
import java.util.TimerTask;

import server.memory.ObjectCache;

/**
 * A singleton subclass of the ObjectCache class. Caches servlets
 * 
 * @author dkirby
 *
 */
public class ServletCache extends ObjectCache 
{
	static private final int _cleanFrequency = 20 * 60 * 1000; // frequency to clean the cache (in ms)
	
	static private ServletCache _instance = null;
	
	private ServletCache()
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
