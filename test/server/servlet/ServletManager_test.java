package server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ServletManager_test {

	private ServletManager s;
	
	@Before
	public void setUp() throws Exception {
		s = ServletManager.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test that getInstance returns the singleton instance as expected
	 */
	public void test_getInstance() {
		ServletManager s1 = ServletManager.getInstance();
		
		assertSame(s, s1);
	}
	
	@Test
	/**
	 * Test that isServletRequest() returns the expected results
	 */
	public void test_isServletRequest()
	{
		try
		{
			String url = "Test";
			
			ServletManager manager = spy(s);
			
			// add a servlet entry to the HashMap
			HashMap<String, ServletManager.ServletFields> servlets = new HashMap<>();
			servlets.put(url, mock(ServletManager.ServletFields.class));
			
			// mock out the getServlets() method to return the above created servlets HashMap
			doReturn(servlets).when(manager).getServlets();
			
			assertTrue(manager.isServletRequest(url));
			assertFalse(manager.isServletRequest("Foo"));
		}
		catch (Exception e)
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that getServlet returns the expected servlet object
	 */
	public void test_getServlet()
	{
		try
		{
			String url = "Test";
			
			ServletManager manager = spy(s);
			
			// create the servlet that should be returned
			ISimpleServlet servlet = mock(ISimpleServlet.class);
			
			// wrap it in a CacheableServlet
			CacheableServlet cServlet = mock(CacheableServlet.class);
			when(cServlet.get_Servlet()).thenReturn(servlet);
			
			// add a servlet entry to the HashMap
			HashMap<String, ServletManager.ServletFields> servlets = new HashMap<>();
			ServletManager.ServletFields sFields = mock(ServletManager.ServletFields.class);
			
			when(sFields.getName()).thenReturn(url);	// mock out the getName() method of ServletFields
			when(sFields.getClassPath()).thenReturn(url); // mock out the getClassPath method of servletFields
			
			servlets.put(url, sFields); // add entry to hashmap
			
			// mock out the ServletCache
			ServletCache cache = mock(ServletCache.class);
			when(cache.get(url)).thenReturn(null);			
						
			// mock out getServlets to return the above created hashmap
			doReturn(servlets).when(manager).getServlets();
			
			// mock out the loadServlet method to return a known servlet
			doReturn(cServlet).when(manager).loadServlet(url);
			
			
			// Test
			// get the servlet
			ISimpleServlet servlet2 = manager.getServlet(url);
			
			// assert that it is the expected one
			assertSame(servlet, servlet2);
		}
		catch (Exception e)
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that getServlet throws ClassNotFoundExceptions as expected
	 */
	public void test_getServlet_Error()
	{
		try
		{
			String url = "Test";
			
			ServletManager manager = spy(s);
			
			// create a blank servlet HashMap
			HashMap<String, ServletManager.ServletFields> servlets = new HashMap<>();	
						
			// mock out getServlets to return the above created hashmap
			doReturn(servlets).when(manager).getServlets();
			
			// get a servlet
			manager.getServlet(url);

			fail();
		}
		catch (ClassNotFoundException e)
		{
			// pass
		}
		catch (Exception e)
		{
			fail();
		}
	}

}
