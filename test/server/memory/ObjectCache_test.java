package server.memory;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ObjectCache_test {
	
	private ObjectCache _Cache;
	
	@Before
	public void setUp() throws Exception {
		_Cache = new ObjectCache();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_cache() {
		Object o1 = new Object();
		Object o2 = new Object();
		
		_Cache.cache("First", o1);
		_Cache.cache("Second", o2);
		
		assertSame(o1, _Cache._cache.get("First"));
		assertSame(o2, _Cache._cache.get("Second"));
		assertNull(_Cache._cache.get("Other"));
	}
	
	@Test
	public void test_get() 
	{
		Object o1 = new Object();
		Object o2 = new Object();
		
		_Cache._cache.put("One", o1);
		_Cache._cache.put("Two", o2);
		
		assertSame(o1, _Cache.get("One"));
		assertSame(o2, _Cache.get("Two"));
		assertNull(_Cache.get("Other"));
	}

}
