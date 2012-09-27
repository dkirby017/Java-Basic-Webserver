package server.memory;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.memory.ObjectCache.CacheNode;

/**
 * Test the ObjectCache class
 * 
 * @author dkirby
 *
 */
public class ObjectCache_test {
	
	/**
	 * Tester class to provide a means to create new CacheNode objects for testing
	 */
	private class ObjectCache_Tester extends ObjectCache {
		public CacheNode createCacheNode(String label, Cacheable object)
		{
			return new CacheNode(0, label, object);
		}
	}
	
	private ObjectCache_Tester _Cache;
	
	@Before
	public void setUp() throws Exception {
		_Cache = new ObjectCache_Tester();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test the cache() method
	 */
	public void test_cache() {
		Cacheable o1 = mock(Cacheable.class);
		Cacheable o2 = mock(Cacheable.class);
		
		_Cache.cache("First", o1);
		_Cache.cache("Second", o2);
		
		assertSame(o1, _Cache._cache.get("First").get_object());
		assertSame(o2, _Cache._cache.get("Second").get_object());
		assertNull(_Cache._cache.get("Other"));
	}
	
	@Test
	/**
	 * Test the get() method
	 */
	public void test_get() 
	{
		Cacheable o1 = mock(Cacheable.class);
		Cacheable o2 = mock(Cacheable.class);
		
		CacheNode node1 = _Cache.createCacheNode("One", o1);
		CacheNode node2 = _Cache.createCacheNode("Two", o2);
		
		_Cache._cache.put("One", node1);
		_Cache._cache.put("Two", node2);
		
		assertSame(o1, _Cache.get("One"));
		assertSame(o2, _Cache.get("Two"));
		assertNull(_Cache.get("Other"));
	}
	
	@Test
	/**
	 * Test the clean() method
	 */
	public void test_clean()
	{
		Cacheable o1 = mock(Cacheable.class);
		Cacheable o2 = mock(Cacheable.class);
		Cacheable o3 = mock(Cacheable.class);
		
		when(o1.isExpired()).thenReturn(true);
		when(o2.isExpired()).thenReturn(false);
		when(o3.isExpired()).thenReturn(true);
		
		_Cache.cache("One", o1);
		_Cache.cache("Two", o2);
		_Cache.cache("Three", o3);
		
		// should only have o2 cached after the clean
		_Cache.clean();
	
		assertNull(_Cache.get("One"));
		assertSame(o2, _Cache.get("Two"));
		assertNull(_Cache.get("Three"));
	}

}
