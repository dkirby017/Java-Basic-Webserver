package server;

import java.util.NoSuchElementException;

import server.io.ClientConnection;
import server.io.ConnectionManager;
import server.io.IErrorLogger;
import server.memory.ThreadManager;
import server.processor.RequestProcessor;

public class Controller {

	private ThreadManager _ThreadManager;
	
	private ConnectionManager _ConnectionManager;
	private IErrorLogger _logger;
	
	public Controller(int maxRequests, ConnectionManager manager, IErrorLogger logger)
	{
		_ThreadManager = new ThreadManager(maxRequests);
		
		_ConnectionManager = manager;
		_logger = logger;
	}
	
	public void exec() throws InterruptedException
	{	
		while (true)
		{	
			ClientConnection connection = null;
				
			try 
			{
				connection = _ConnectionManager.pop();
			} 
			catch (NoSuchElementException | InterruptedException e) 
			{
				_logger.log(e.getMessage());
				continue;
			}
			
			_ThreadManager.waitForFreeThread();
			
			RequestProcessor requestProcessor = new RequestProcessor(_ThreadManager, connection, _logger);
			requestProcessor.start();
		}
	}
}
