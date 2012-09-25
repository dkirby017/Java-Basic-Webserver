package server.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import server.io.HTTPStatusCodes.StatusCode;

/**
 * Implementation of the IResponse interface
 * 
 * @author dkirby
 *
 */
public class SimpleResponse implements IResponse {
	
	private final OutputStream _OutputStream;
	
	private StatusCode _StatusCode;
	protected ArrayList<String[]> _headers;
	
	/**
	 * Constructor
	 * 
	 * @param socket the socket connection to build the response around
	 * @throws IOException if an I/O error occurs during construction
	 */
	public SimpleResponse(Socket socket) throws IOException {
		_OutputStream = socket.getOutputStream();
		_headers = new ArrayList<String[]>();
	}

	@Override
	public void setStatusCode(int httpStatusCode) {
		_StatusCode = HTTPStatusCodes.mapStatusCode(httpStatusCode);
	}

	@Override
	public void setHeader(String name, String value) {
		for (int i = 0; i < _headers.size(); i++)
		{
			if (_headers.get(i)[0].equals(name))
			{
				_headers.get(i)[1] = value;
				return;
			}
		}
		
		String[] s = { name, value };
		_headers.add(s);
	}

	@Override
	public void setMimeType(String mimeType) {
		setHeader("Content-Type", mimeType);
	}
	
	@Override
	public String generateHeader()
	{
		// build the first line of the header
		String s = "HTTP/1.1 " + HTTPStatusCodes.mapStatusMessage(_StatusCode) + "\r\n";				
		
		// build the rest of the header
		for (int i = 0; i < _headers.size(); i++)
		{
			s += _headers.get(i)[0] + ": " + _headers.get(i)[1] + "\r\n";
		}
		
		// build the trailing \r\n
		s += "\r\n";
		
		return s;
	}
	
	@Override
	public OutputStream getOutputStream() {		
		return _OutputStream;
	}

}
