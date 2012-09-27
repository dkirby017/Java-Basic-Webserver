package server.resource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the ResourceFactory class
 * 
 * @author dkirby
 *
 */
public class ResourceFactory_test {

	private ResourceFactory _Factory;
	private CacheableResource _Resource;
	private File _File;
	
	@Before
	public void setUp() throws Exception {
		_Resource = mock(CacheableResource.class);
		_File = mock(File.class);
		
		_Factory = spy(new ResourceFactory());		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test that newInstance() returns the expected resource object
	 */
	public void test_newInstance() {
		try
		{
			doReturn(_File).when(_Factory).getFile("");
			doReturn(_Resource).when(_Factory).buildNewResource(_File);
			
			CacheableResource resource = _Factory.newInstance("");
			
			assertSame(_Resource, resource);
		}
		catch (Exception e)
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that newInstance() passes the appriopriate exceptions up the call stack as expected
	 */
	public void test_newInstance_error() {
		try
		{
			doThrow(new FileNotFoundException()).when(_Factory).getFile("");
			
			try
			{
				_Factory.newInstance("");
				
				fail();
			}
			catch (FileNotFoundException e)
			{
				// pass
			}
			
			doReturn(_File).when(_Factory).getFile("");
			doThrow(new IOException()).when(_Factory).buildNewResource(_File);
			
			try
			{
				_Factory.newInstance("");
				
				fail();
			}
			catch (IOException e)
			{
				// pass
			}			
		}
		catch (Exception e)
		{
			fail();
		}
	}

}
