package server.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author dkirby
 *
 * Factory: Generates ClientConnection objects from incoming ServerSocket connections
 */
public class ClientConnectionFactory {
	
	private ServerSocket _ServerSocket = null;
	
	/**
	 * Constructor
	 * 
	 * @param serverSocket The ServerSocket to use when generating the ClientConnection objects
	 */
	public ClientConnectionFactory(ServerSocket serverSocket) {
		_ServerSocket = serverSocket;
	}
	
	/**
	 * Waits for an incoming connection and generates a new ClientConnection object
	 * 
	 * @return a new ClientConnection object
	 * 
	 * @throws IOException if an I/O error occurs while waiting for a connection
	 */
	public ClientConnection newInstance() throws IOException
	{			
		Socket socket = _ServerSocket.accept();
		
		return buildClientConnection(socket);
	}
	
	/**
	 * Helper method to isolate the construction of the ClientConnection object.
	 * Useful for testing
	 * 
	 * @param socket the socket connection to build the ClientConnection around
	 * 
	 * @return a new ClientConnection object
	 */
	ClientConnection buildClientConnection(Socket socket)
	{				
		return new ClientConnection(socket);		
	}
}
