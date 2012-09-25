package server.io;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the ClientConnectionFactory class
 * 
 * @author dkirby
 *
 */
public class ClientConnectionFactory_test {

	private ServerSocket _ServerSocket;
	private Socket _Socket;
	private ClientConnectionFactory _ClientConnectionFactory;
	private ClientConnection _ClientConnection;
	
	@Before
	public void setUp() throws Exception {
		/**
		 * Mock out a number of objects to use in these tests
		 */
		_ServerSocket = mock(ServerSocket.class);
		_Socket = mock(Socket.class);
		_ClientConnection = mock(ClientConnection.class);
		
		/**
		 * Declare a spy object so that we can mock out the helper methods
		 */
		_ClientConnectionFactory = spy(new ClientConnectionFactory(_ServerSocket));
	}

	@Test
	/**
	 * Test that the getNewConnection method returns the expected result
	 */
	public void test_getNewConnection() {
		try 
		{
			/**
			 * Define mock behaviour so that the getNewConnection method can be tested in isolation
			 */
			when(_ServerSocket.accept()).thenReturn(_Socket);
			doReturn(_ClientConnection).when(_ClientConnectionFactory).buildClientConnection(_Socket);
			
			ClientConnection clientConnection = _ClientConnectionFactory.newInstance();
			
			assertSame(_ClientConnection, clientConnection);			
		} 
		catch (Exception e) 
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that the getNewConnection method passes an IOException up the call stack as expected
	 */
	public void test_getNewConnection_Error() {
		try 
		{
			/**
			 * Define mock behaviour so that the getNewConnection method can be tested in isolation.
			 * When the _ServerSocket mock object attempts to get a socket, throw an IOException
			 */
			when(_ServerSocket.accept()).thenThrow(new IOException());
			doReturn(_ClientConnection).when(_ClientConnectionFactory).buildClientConnection(_Socket);
			
			_ClientConnectionFactory.newInstance();
			
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

}
