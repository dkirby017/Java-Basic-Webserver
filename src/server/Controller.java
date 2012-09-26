package server;

import java.util.NoSuchElementException;

import server.io.ClientConnection;
import server.io.ConnectionManager;
import server.io.IErrorLogger;
import server.memory.ThreadManager;
import server.processor.RequestProcessor;

/**
 * Receives ClientConnection's from the ConnectionManager and spawns a new thread for each request. The number of active
 * threads is limited by the ThreadManager
 * 
 * @author dkirby
 *
 */
public class Controller {

	private ThreadManager _ThreadManager;
	
	private ConnectionManager _ConnectionManager;
	private IErrorLogger _logger;
	
	/**
	 * Constructor
	 * 
	 * @param threadManager The thread manager to use to control when to spawn threads
	 * @param manager the ConnectionManager to draw the ClientConnections from
	 * @param logger the ErrorLogger to use to log error messages
	 */
	public Controller(ThreadManager threadManager, ConnectionManager manager, IErrorLogger logger)
	{
		_ThreadManager = threadManager;
		_ConnectionManager = manager;
		_logger = logger;
	}
	
	/**
	 * Loops indefinitely, calling Process() to process a new client request
	 */
	public void Execute() 
	{	
		while (true)
		{	
			Process();
		}
	}
	
	/**
	 * Processes a client request. Draws ClientConnections from the ConnectionManager, utilises the ThreadManager
	 * to determine whether it is safe to spawn a new thread, and then spawns a new thread to process the request
	 */
	void Process()
	{
		ClientConnection connection = null;
		
		try 
		{
			connection = _ConnectionManager.pop();
			
			_ThreadManager.waitForFreeThread();
		} 
		catch (NoSuchElementException | InterruptedException e) 
		{
			_logger.log(e.getMessage());
			return;
		}			
		
		RequestProcessor requestProcessor = buildRequestProcessor(connection);
		requestProcessor.start();
	}
	
	/**
	 * Generates a new RequestProcessor object for the given ClientConnection
	 * 
	 * @param connection the ClientConnection to be processed
	 * 
	 * @return a new RequestProcessor object
	 */
	RequestProcessor buildRequestProcessor(ClientConnection connection)
	{
		return new RequestProcessor(_ThreadManager, connection, _logger);
	}
}
