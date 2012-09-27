package server.servlet;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the ServletCache class
 * 
 * @author dkirby
 *
 */
public class ServletCache_test {

	private ServletCache _cache;
	
	@Before
	public void setUp() throws Exception {
		_cache = ServletCache.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test that the ServletCache correctly implements a singleton
	 */
	public void test_getInstance() {
		ServletCache cache2 = ServletCache.getInstance();
		
		assertSame(cache2, _cache);
	}

}
