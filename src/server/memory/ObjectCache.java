package server.memory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implements a synchronized cache of Objects
 * 
 * @author dkirby
 *
 */
public class ObjectCache 
{	
	protected class CacheNode {
		private int _index;
		private String _label;
		private Cacheable _object;
		
		public CacheNode(int index, String label, Cacheable object) {
			_index = index;
			_label = label;
			_object = object;
		}
		
		public int get_index() {
			return _index;
		}
		
		public String get_label() {
			return _label;
		}
		
		public Cacheable get_object() {
			return _object;
		}
		
		public void set_index(int index) {
			_index = index;
		}
		
		public void set_label(String label) {
			_label = label;
		}
		
		public void set_object(Cacheable object) {
			_object = object;
		}
	}
	
	protected HashMap<String, CacheNode> _cache;
	protected ArrayList<CacheNode> _cacheList;
	
	public ObjectCache() {
		_cache = new HashMap<>();
		_cacheList = new ArrayList<>();
	}
	
	/**
	 * Caches the input object
	 * 
	 * @param label a string to identify the object
	 * @param object the object to cache
	 */
	synchronized public void cache(String label, Cacheable object)
	{		
		if (_cache.containsKey(label))
		{
			CacheNode node = _cache.get(label);
			node.set_object(object);
		}
		else
		{
			int index = _cacheList.size();
			CacheNode node = new CacheNode(index, label, object);
			
			_cacheList.add(node);
			_cache.put(label, node);
		}
	}
	
	/**
	 * Gets a cached object
	 * 
	 * @param label the identifying string for the object
	 * @return the cached object, null if it doesn't exist
	 */
	synchronized public Cacheable get(String label)
	{
		if (_cache.containsKey(label))
		{
			CacheNode node = _cache.get(label);
			if (node != null)
				return node.get_object();
		}
		
		return null;
	}	
	
	/**
	 * Removes all expired items from the cache, including null objects
	 */
	synchronized public void clean()
	{
		for (int i = 0; i < _cacheList.size(); i++)
		{	
			CacheNode node = _cacheList.get(i);
			
			Cacheable object = node.get_object();
			
			if (object == null || object.isExpired())
			{
				removeObjectByLabel(node.get_label());
				i--;			
			}			
		}
	}
	
	/**
	 * Removes a cached object specified by its label
	 * 
	 * @param label the label of the Object
	 */
	void removeObjectByLabel(String label)
	{
		CacheNode node = _cache.get(label);
		if (node == null)
			return;
		
		int index = node.get_index();
		int end = _cacheList.size() - 1;
		
		// remove from HashMap
		_cache.remove(label);
		
		// remove from ArrayList
		_cacheList.set(index, _cacheList.get(end));
		_cacheList.remove(end);		
		_cacheList.get(index).set_index(index);
	}
}
