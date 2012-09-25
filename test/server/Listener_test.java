package server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import server.io.ClientConnection;
import server.io.ClientConnectionFactory;
import server.io.ConnectionManager;
import server.io.IErrorLogger;

/**
 * Test the Listener class
 * 
 * @author dkirby
 *
 */
public class Listener_test {

	private ConnectionManager _ConnectionManager;
	private ClientConnectionFactory _ClientConnectionFactory;
	private ClientConnection _ClientConnection;
	private IErrorLogger _ErrorLogger;
	
	private Listener _listener;
	
	@Before
	public void setUp() {
		/**
		 * Mock out a number of objects so that the Listener class can be tested in isolation
		 */
		_ConnectionManager = mock(ConnectionManager.class);
		_ClientConnectionFactory = mock(ClientConnectionFactory.class);
		_ClientConnection = mock(ClientConnection.class);
		_ErrorLogger = mock(IErrorLogger.class);
		
		_listener = new Listener(_ClientConnectionFactory, _ConnectionManager, _ErrorLogger);
	}
	
	@Test
	/**
	 * Test the listen() method. Since run() just loops and calls listen(), testing listen() will be sufficient to
	 * test the class' public methods
	 */
	public void test_listen()
	{	
		try
		{
			/**
			 * Define a known mocked behaviour for the factory's getNewConnection() method, providing
			 * a known ClientConnection object
			 */
			when( _ClientConnectionFactory.newInstance() ).thenReturn(_ClientConnection);
				
			_listener.listen();
				
			// verify that a ClientConnection was pushed to the connection manager
			verify(_ConnectionManager).push(_ClientConnection);
		}
		catch (Throwable e)
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that the listen() method logs the IOException as expected
	 */
	public void test_listen_error()
	{
		try 
		{
			String msg = "Error String";
			when( _ClientConnectionFactory.newInstance() ).thenThrow(new IOException(msg));	
			
			_listener.listen();
			
			verify(_ErrorLogger).log(msg);
		}
		catch (IOException e)
		{
			fail(); // the listener should catch and log the error
		}
	}

}
