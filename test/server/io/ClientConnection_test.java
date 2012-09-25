package server.io;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the ClientConnection class
 * 
 * @author dkirby
 *
 */
public class ClientConnection_test {

	private ClientConnection _con;
	private Socket _socket;
	private SimpleRequest _req;
	private SimpleResponse _res;
	
	@Before
	public void setUp() throws Exception {
		/**
		 * Mock out a number of objects to use for these tests
		 */
		_req = mock(SimpleRequest.class);
		_res = mock(SimpleResponse.class);
		_socket = mock(Socket.class);
		
		/**
		 * Declare a spy object so that we can mock out the helper methods
		 */
		_con = spy(new ClientConnection(_socket));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test that the get_request method returns the expected result
	 */
	public void test_get_request() {
		try 
		{
			/**
			 * Mock out the build_request method so that get_request can be tested in isolation.
			 * Return a known request object instead of running the build_request code.
			 */
			doReturn(_req).when(_con).buildRequest();
			
			assertSame(_req, _con.getRequest());
		} 
		catch (Exception e) 
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that the get_request method passes an IOException up the call stack as expected
	 */
	public void test_get_request_error() {
		try 
		{
			/**
			 * Mock out the build_request method so that get_request can be tested in isolation.
			 * Throw an IOException instead of running the build_request code.
			 */
			doThrow(new IOException()).when(_con).buildRequest();
			
			_con.getRequest();
			
			fail();
		} 
		catch (IOException e) 
		{
			// pass
		}
		catch (Exception e)
		{
			/**
			 * Any other type of exception is an error
			 */
			fail();
		}
	}
	
	@Test
	/**
	 * Test that the get_response method returns the expected result
	 */
	public void test_get_response() {
		try 
		{
			/**
			 * Mock out the build_response method so that get_response can be tested in isolation.
			 * Return a known response object instead of running the build_response code.
			 */
			doReturn(_res).when(_con).buildResponse();
			
			assertSame(_res, _con.getResponse());
		} 
		catch (IOException e) 
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that the get_response method passes an IOException up the call stack as expected
	 */
	public void test_get_response_error() {
		try 
		{
			/**
			 * Mock out the build_response method so that get_response can be tested in isolation.
			 * Throw an IOException instead of running the build_response code.
			 */
			doThrow(new IOException()).when(_con).buildResponse();
			
			_con.getResponse();
			
			fail();
		} 
		catch (IOException e) 
		{
			// pass
		}
	}

}
