package server;

import java.io.IOException;
import java.net.ServerSocket;

import server.io.ClientConnectionFactory;
import server.io.ConnectionManager;
import server.io.ConsoleLogger;
import server.io.IErrorLogger;
import server.memory.ThreadManager;

/**
 * Initializes the WebServer and spawns a Listener thread, before passing control to the Controller
 * 
 * @author dkirby
 *
 */
public class WebServer {
	
	static private ConnectionManager _Manager;
	static private IErrorLogger _Logger;	
	
	/**
	 * Main program method. Sets up and starts the web server
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {		
		
		int port = 80; // The port to listen on
		int maxRequests = 100; // the maximum number of requests to process simultaneously
		
		// initialise the common objects
		init();		
		
		// set up listener
		if (!spawnListener(port))
			return;
		
		// set up the controller and pass control to it
		startController(maxRequests);	
	}
	
	/**
	 * Initialises the common objects for the listener and controller.
	 */
	static void init()
	{
		_Manager = new ConnectionManager();
		_Logger = new ConsoleLogger();		
	}
	
	/**
	 * Sets up the listener
	 * 
	 * @param port the port to listen on
	 * 
	 * @return returns true if the setup succeeds, false otherwise
	 */
	static boolean spawnListener(int port)
	{
		ServerSocket serverSocket = null;
		try
		{
			// attempt to connect to the specified port number
			serverSocket = new ServerSocket(port);
		}
		catch (IOException e)
		{
			// unable to connect to port
			_Logger.log(e.getMessage());
			return false;
		}
		
		ClientConnectionFactory factory = new ClientConnectionFactory(serverSocket);
		
		// Listener will run in a separate thread
		Thread listener = new Thread(new Listener(factory, _Manager, _Logger));
		listener.start();
		
		System.out.println("Server now listening on port " + port);	
		
		return true;
	}
	
	/**
	 * Starts the controller
	 * 
	 * @param maxRequests the maximum number of requests to process simultaneously
	 */
	static void startController(int maxRequests)
	{
		ThreadManager threadManager = new ThreadManager(maxRequests);
		
		// pass control to the controller
		Controller controller = new Controller(threadManager, _Manager, _Logger);		
		controller.Execute();	
	}
}