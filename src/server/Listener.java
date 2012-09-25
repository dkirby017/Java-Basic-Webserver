package server;

import java.io.IOException;

import server.io.ClientConnection;
import server.io.ClientConnectionFactory;
import server.io.ConnectionManager;
import server.io.IErrorLogger;

/**
 * Listens for client connections (using the ClientConnectionFactory) and adds them to the ConnectionManager's list
 * 
 * @author dkirby
 *
 */
public class Listener implements Runnable {

	private ClientConnectionFactory _ClientConnectionFactory;
	private ConnectionManager _ConnectionManager;
	private IErrorLogger _ErrorLogger;
	
	/**
	 * Constructor
	 * 
	 * @param factory the ClientConnectionFactory object to listen with
	 * @param manager the ConnectionManager object to push connections to
	 * @param logger the IErrorLogger object to use to log any errors
	 */
	public Listener(ClientConnectionFactory factory, ConnectionManager manager, IErrorLogger logger) {
		_ClientConnectionFactory = factory;
		_ConnectionManager = manager;
		_ErrorLogger = logger;
	}

	@Override
	public void run() 
	{		
		while (true)
		{
			listen();
		}
	}

	/**
	 * Listens for a connection and pushes it to the manager's list. Logs any errors
	 */
	void listen() {
		try 
		{
			ClientConnection clientConnection = _ClientConnectionFactory.newInstance();	
			_ConnectionManager.push(clientConnection);
		}
		catch (IOException e)
		{
			_ErrorLogger.log(e.getMessage());
		}
	}
}