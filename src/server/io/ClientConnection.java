package server.io;

import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author dkirby
 *
 * Represents the connection with the client, providing access to the request and response objects.
 * 
 * Insulates the rest of the program from how the connections are established in the factory, and hides the 
 * representation of the connection used by the program from the factory.
 */
public class ClientConnection {
	
	private Socket _socket;
	private SimpleRequest _request = null;
	private SimpleResponse _response = null;
	
	/**
	 * Constructor
	 * 
	 * @param socket the socket used for the connection
	 * @throws UnsupportedMethodException 
	 * @throws MalformedRequestException 
	 * @throws IOException 
	 */
	public ClientConnection(Socket socket) {
		_socket = socket;
	}
	
	/**
	 * Helper method to isolate the construction of the SimpleRequest object. Useful in testing
	 * 
	 * @return a SimpleRequest object  
	 * @throws IOException if an I/O error occurs while constructing the SimpleRequest
	 * @throws MalformedRequestException if the request is malformed
	 * @throws UnsupportedMethodException if the request method is unsupported
	 */
	SimpleRequest buildRequest() throws IOException, MalformedRequestException, UnsupportedMethodException {				
		return new SimpleRequest(_socket);
	}
	
	/**
	 * Helper method to isolate the construction of the SimpleResponse object. Useful in testing
	 * 
	 * @return a SimpleResponse object
	 * @throws IOException if an I/O error occurs while constructing the SimpleResponse
	 */
	SimpleResponse buildResponse() throws IOException {
		return new SimpleResponse(_socket);
	}
	
	/**
	 * Gets the instance's SimpleRequest object
	 * 
	 * @return the instance's SimpleRequest object
	 * @throws IOException if an I/O error occurs while getting the SimpleRequest
	 * @throws MalformedRequestException if the request is malformed
	 * @throws UnsupportedMethodException if the request method is unsupported
	 */
	public SimpleRequest getRequest() throws IOException, MalformedRequestException, UnsupportedMethodException {
		if (_request == null)
			_request = buildRequest();
		
		return _request;
	}
	
	/**
	 * Gets the instances SimpleResponse object
	 * 
	 * @return the instance's SimpleResponse object
	 * @throws IOException if an I/O error occurs while getting the SimpleResponse
	 */
	public SimpleResponse getResponse() throws IOException {
		if (_response == null)
			_response = buildResponse();
		
		return _response;
	}
	
	/**
	 * Closes the client connection
	 * 
	 * @throws IOException if an I/O error occurs while closing the connection 
	 */
	public void close() throws IOException
	{
		if (!_socket.isClosed())
			_socket.close();
	}
	
	/**
	 * Returns whether the client connection is closed
	 * 
	 * @return true if the connection is closed, false otherwise
	 */
	public boolean isClosed()
	{
		return _socket.isClosed();
	}
	
}
