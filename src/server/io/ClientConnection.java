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
	private IRequest _request = null;
	private IResponse _response = null;
	
	/**
	 * Constructor
	 * 
	 * @param socket the socket used for the connection
	 */
	public ClientConnection(Socket socket) {
		_socket = socket;
	}
	
	/**
	 * Helper method to isolate the construction of the IRequest object. Useful in testing
	 * 
	 * @return an IRequest object  
	 * @throws IOException if an I/O error occurs while constructing the IRequest
	 * @throws MalformedRequestException if the request is malformed
	 * @throws UnsupportedMethodException if the request method is unsupported
	 */
	IRequest buildRequest() throws IOException, MalformedRequestException, UnsupportedMethodException {				
		return new SimpleRequest(_socket);
	}
	
	/**
	 * Helper method to isolate the construction of the IResponse object. Useful in testing
	 * 
	 * @return a IResponse object
	 * @throws IOException if an I/O error occurs while constructing the IResponse
	 */
	IResponse buildResponse() throws IOException {
		return new SimpleResponse(_socket);
	}
	
	/**
	 * Gets the instance's IRequest object
	 * 
	 * @return the instance's IRequest object
	 * @throws IOException if an I/O error occurs while getting the IRequest
	 * @throws MalformedRequestException if the request is malformed
	 * @throws UnsupportedMethodException if the request method is unsupported
	 */
	public IRequest getRequest() throws IOException, MalformedRequestException, UnsupportedMethodException {
		if (_request == null)
			_request = buildRequest();
		
		return _request;
	}
	
	/**
	 * Gets the instances IResponse object
	 * 
	 * @return the instance's IResponse object
	 * @throws IOException if an I/O error occurs while getting the IResponse
	 */
	public IResponse getResponse() throws IOException {
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
