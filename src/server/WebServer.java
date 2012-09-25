package server;

import java.io.IOException;
import java.net.ServerSocket;

import server.io.ClientConnectionFactory;
import server.io.ConnectionManager;
import server.io.ConsoleLogger;

public class WebServer {

	public static void main(String[] args) {		
		
		int port = 80;
		int maxRequests = 100;
		
		ConnectionManager manager = new ConnectionManager();
		ConsoleLogger logger = new ConsoleLogger();
		
		ServerSocket serverSocket = null;
		ClientConnectionFactory factory = null;
		
		try 
		{
			serverSocket = new ServerSocket(port);		
		
			factory = new ClientConnectionFactory(serverSocket);
		}
		catch (IOException e)
		{
			logger.log(e.getMessage());
			return;
		}		
		
		// Listener will run in a separate thread
		Thread listener = new Thread(new Listener(factory, manager, logger));
		listener.start();
		
		System.out.println("Server now listening on port " + port);
		
		// Scheduler will run in this thread
		Controller scheduler = new Controller(maxRequests, manager, logger);
		
		try
		{
			scheduler.exec();
		}
		catch (InterruptedException e)
		{
			logger.log(e.getMessage());
			return;
		}
		finally
		{
			if (!serverSocket.isClosed())
				try
				{
					serverSocket.close();
				}
				catch (IOException e) 
				{
					logger.log(e.getMessage());
				}
		}
	}
}