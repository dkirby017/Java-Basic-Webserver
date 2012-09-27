package server.resource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.io.ClientConnection;
import server.io.IRequest;
import server.io.IResponse;

/**
 * Test the ResourceProcessor class
 * 
 * @author dkirby
 *
 */
public class ResourceProcessor_test {

	private ResourceProcessor _Processor;
	
	@Before
	public void setUp() throws Exception {		
		_Processor = spy(new ResourceProcessor());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test that the process() method attempts to get a resource and output it to the client
	 */
	public void test_Process() {
		try {
			// setup for this test only
			ResourceCache cache = mock(ResourceCache.class);		
			ResourceFactory factory = mock(ResourceFactory.class);
			Resource resource = mock(Resource.class);
			
			when(factory.newInstance("")).thenReturn(resource);
			
			IRequest request = mock(IRequest.class);
			when(request.getUrlPath()).thenReturn("");			
			
			IResponse response = mock(IResponse.class);
			
			ClientConnection connection = mock(ClientConnection.class);
			when(connection.getRequest()).thenReturn(request);
			when(connection.getResponse()).thenReturn(response);
						
			doReturn(cache).when(_Processor).getResourceCache();
			doReturn(factory).when(_Processor).getResourceFactory();
			doReturn("").when(_Processor).getExtension("");
			doNothing().when(_Processor).outputResource(response, resource, "");
			
			// test
			_Processor.process(connection);
			
			verify(cache).get("");
			verify(_Processor).outputResource(response, resource, "");
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	/**
	 * Test that OutputResouce() outputs the expected string to the client
	 */
	public void test_OutputResource() {
		try {
			// setup for this test only
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			IResponse response = mock(IResponse.class);
			when(response.generateHeader()).thenReturn("Header\n");
			when(response.getOutputStream()).thenReturn(outputStream);
			
			Resource resource = mock(Resource.class);
			when(resource.getBytes()).thenReturn("Resource".getBytes());
						
			// test
			_Processor.outputResource(response, resource, "");
			
			assertEquals("Header\nResource", outputStream.toString());
			
		} catch (Exception e) {
			fail();
		}
	}

}
