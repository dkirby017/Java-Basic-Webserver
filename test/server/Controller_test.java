package server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import server.io.ClientConnection;
import server.io.ConnectionManager;
import server.io.IErrorLogger;
import server.memory.ThreadManager;
import server.processor.RequestProcessor;

public class Controller_test {

	private Controller _Controller;	
	private RequestProcessor _RequestProcessor;
	private ClientConnection _ClientConnection;
	ThreadManager _ThreadManager;
	ConnectionManager _ConnectionManager;
	IErrorLogger _ErrorLogger;
	
	@Before
	public void setUp() throws Exception {
		 _ThreadManager = mock(ThreadManager.class);
		 _ConnectionManager = mock(ConnectionManager.class);
		 _ErrorLogger = mock(IErrorLogger.class);			
		_RequestProcessor = mock(RequestProcessor.class);
		_ClientConnection = mock(ClientConnection.class);	
		
		_Controller = spy(new Controller(_ThreadManager, _ConnectionManager, _ErrorLogger));		
	}

	@Test
	/**
	 * Test that exec() gets a ClientConnection from the ConnectionManager, uses the ThreadManager to check if its safe
	 * to spawn a thread, gets a RequestProcessor object, and starts the new thread.
	 */
	public void test_Process() 
	{
		try
		{
			when(_ConnectionManager.pop()).thenReturn(_ClientConnection);
			doReturn(_RequestProcessor).when(_Controller).buildRequestProcessor(_ClientConnection);
			
			_Controller.Process();
			
			verify(_ConnectionManager).pop();
			verify(_ThreadManager).waitForFreeThread();
			verify(_RequestProcessor).start();
		}
		catch (Exception e)
		{
			fail();
		}
	}

}
