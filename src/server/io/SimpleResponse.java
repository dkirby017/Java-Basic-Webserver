package server.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import server.io.HTTPStatusCodes.StatusCode;

/**
 * Implementation of the IResponse interface
 * 
 * @author dkirby
 *
 */
public class SimpleResponse implements IResponse {
	
	/** 
	 * private class to store the header value along with its index in the _headerArray ArrayList.
	 * Used in a HashMap and ArrayList to allow for constant time insertion and lookup, and printing the entire
	 * list in linear time
	 * 
	 * @author dkirby
	 *
	 */
	private class headerNode {
		private int _arrayIndex;
		private String _key;
		private String _value;
		
		public headerNode(String key, String value, int arrayIndex) {
			_key = key;
			_value = value;
			_arrayIndex = arrayIndex;
		}
		
		@SuppressWarnings("unused")
		public void setKey(String key)
		{
			_key = key;
		}
		
		public void setValue(String value)
		{
			_value = value;
		}
		
		@SuppressWarnings("unused")
		public void setIndex(int arrayIndex)
		{
			_arrayIndex = arrayIndex;
		}
		
		public String getKey()
		{
			return _key;
		}
		
		public String getValue()
		{
			return _value;
		}
		
		@SuppressWarnings("unused")
		public int getIndex()
		{
			return _arrayIndex;
		}
	}
	
	private final OutputStream _OutputStream;
	
	private StatusCode _StatusCode;
	private HashMap<String, headerNode> _headers;	
	private ArrayList<headerNode> _headerArray;
	
	
	/**
	 * Constructor
	 * 
	 * @param socket the socket connection to build the response around
	 * @throws IOException if an I/O error occurs during construction
	 */
	public SimpleResponse(Socket socket) throws IOException {
		_OutputStream = socket.getOutputStream();
		_headers = new HashMap<>();
		_headerArray = new ArrayList<headerNode>();
	}

	@Override
	public void setStatusCode(int httpStatusCode) {
		_StatusCode = HTTPStatusCodes.mapStatusCode(httpStatusCode);
	}

	@Override
	public void setHeader(String name, String value) {
		if (_headers.containsKey(name))
		{
			headerNode hNode = _headers.get(name);			
			hNode.setValue(value);
		}
		else
		{
			int index = _headerArray.size();
			headerNode hNode = new headerNode(name, value, index);
			
			_headerArray.add(hNode);
			_headers.put(name, hNode);
		}
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
			s += _headerArray.get(i).getKey() + ": " + _headerArray.get(i).getValue() + "\r\n";
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
