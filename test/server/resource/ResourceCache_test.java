package server.resource;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the ResourceCache class
 * 
 * @author dkirby
 *
 */
public class ResourceCache_test {

	private ResourceCache _cache;
	
	@Before
	public void setUp() throws Exception {
		_cache = ResourceCache.getInstance(); 
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test that the ResourceCache correctly implements a singleton
	 */
	public void test_getInstance() {
		ResourceCache cache2 = ResourceCache.getInstance();
		
		assertSame(_cache, cache2);
	}

}
