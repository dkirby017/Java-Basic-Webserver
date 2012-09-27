package server.processor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.io.ClientConnection;
import server.io.IErrorLogger;
import server.io.IRequest;
import server.memory.ThreadManager;
import server.servlet.ServletManager;

/**
 * Test the RequestProcessor class
 * 
 * @author dkirby
 *
 */
public class RequestProcessor_test {

	private RequestProcessor _Processor;
	private IProcessor _IProcessor;
	private ClientConnection _Connection;
	private IErrorLogger _Logger;
	
	@Before
	public void setUp() throws Exception {
		ThreadManager manager = mock(ThreadManager.class);
		_Logger = mock(IErrorLogger.class);
		
		// set up a mock request that returns null from getUrlPath
		IRequest request = mock(IRequest.class);
		when(request.getUrlPath()).thenReturn(null);
		
		// set up a mock clientconnection that returns the above request from getRequest()
		_Connection = mock(ClientConnection.class);
		when(_Connection.getRequest()).thenReturn(request);
		
		// set up a mock servletManager that returns false for isServletRequest()
		ServletManager servletManager = mock(ServletManager.class);
		when(servletManager.isServletRequest(null)).thenReturn(false);
		
		// set up a mock IProcessor
		_IProcessor = mock(IProcessor.class);
		
		// initialise the RequestProcessor
		_Processor = spy(new RequestProcessor(manager, _Connection, _Logger));
		
		// mock out the getServletManager() method to return the above servlet manager
		doReturn(servletManager).when(_Processor).getServletManager();
		
		// mock out the getProcessor() method to return the above IProcessor
		doReturn(_IProcessor).when(_Processor).getProcessor(_Connection, servletManager);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test that the process() method obtains an IProcessor object and invokes its process() method
	 */
	public void test_process() 
	{
		try
		{
			// call the RequestProcessor's process() method
			_Processor.process(_Connection);
		
			// verify that it called the process() method for the IProcessor object that should take over processing for the request
			verify(_IProcessor).process(_Connection);
		}
		catch (Exception e)
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that the process() method logs any exceptions
	 */
	public void test_process_error() 
	{
		try
		{
			// set up the IProcessor to throw an exception when processing
			doThrow(new IOException("")).when(_IProcessor).process(_Connection);
			
			when(_Connection.getResponse()).thenThrow(new IOException(""));
			
			// call the RequestProcessor's process() method
			_Processor.process(_Connection);
		
			// verify that the logger was called twice (once for each exception)
			verify(_Logger, times(2)).log("");
		}
		catch (Exception e)
		{
			fail();
		}
	}
}
