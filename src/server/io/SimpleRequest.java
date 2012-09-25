package server.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Scanner;

import server.io.RequestMethods.Method;

/**
 * Implementation of the IRequest interface
 * 
 * @author dkirby
 *
 */
public class SimpleRequest implements IRequest{
	
	private final String ENCODING = "UTF-8";
	
	private InetAddress _InetAddress;
	private Method _method;
	private String _url;
	
	private HashMap<String, String> _urlParams;
	private HashMap<String, String> _headers;
	private HashMap<String, String> _postData;	
	
	/**
	 * Constructor
	 * 
	 * @param socket the socket connection to build the request around
	 * 
	 * @throws IOException if an I/O error occurs during construction
	 * @throws MalformedRequestException if the request is malformed
	 * @throws UnsupportedMethodException if the request method is unsupported
	 */
	public SimpleRequest(Socket socket) throws IOException, MalformedRequestException, UnsupportedMethodException {
		_InetAddress = socket.getInetAddress();
		
		InputStream input = socket.getInputStream();
		parseInput(input);
	}
	
	/**
	 * Parses the request and stores the results locally
	 * 
	 * @param input the InputStream to read from
	 * @throws IOException
	 * @throws MalformedRequestException
	 * @throws UnsupportedMethodException 
	 */
	void parseInput(InputStream input) throws IOException, MalformedRequestException, UnsupportedMethodException 
	{
		String s = readInputStream(input);
		if (s == null || s.isEmpty())
			throw new IOException("No data");
			
		Scanner request = new Scanner(s);
				
		// Parse first line data
		_method = parseMethod(request);
		_url = parseUrl(request);		
		
		// the next token in the request should be HTTP/x.x, so make sure it is
		if (!verifyHTTPFormat(request))
			throw new MalformedRequestException("Malformed request");
		
		// Parse the URL parameters (if any)
		_urlParams = parseUrlParams(_url);
		
		// Parse the remainder of the header
		_headers = parseHeaders(request);
		
		// Parse the post data (if any)
		if (_method == Method.POST)
			_postData = parsePostData(request);
		else
			// set it to an empty hashmap so that the getPostData() method doesn't throw an exception
			_postData = new HashMap<>(); 
	}
	
	/**
	 * Reads the InputStream provided and returns the read data as a String
	 * 
	 * @param input the InputStream to read
	 * @return a String containing the data read from the InputStream
	 * @throws IOException
	 */
	String readInputStream(InputStream input) throws IOException
	{					
		BufferedReader bReader = new BufferedReader(new InputStreamReader(input));
		
		String result = bReader.readLine();
		String tmp = result;
		while (tmp != null && !tmp.isEmpty())
		{
			tmp = bReader.readLine();
			
			if (tmp != null)
			{
				result = result + "\n" + tmp;
			}
		}
		
		return result;
	}
	
	/**
	 * Parses the request method from the request
	 * 
	 * @param request a Scanner wrapped around the string representation of the request
	 * @return the request method
	 * @throws UnsupportedMethodException if the request method is unsupported
	 */
	Method parseMethod(Scanner request) throws UnsupportedMethodException
	{	
		String s = request.next();
		
		RequestMethods.Method method = RequestMethods.mapMethod(s);
		
		if (method == Method.INVALID)
			throw new UnsupportedMethodException("Invalid request method");
		
		return method;
	}
	
	/**
	 * Parse out the URL from the request
	 * 
	 * @param request a Scanner wraped around the string representation of the request. It is assumed that the
	 * request method has already been parsed
	 * 
	 * @return the URL
	 */
	String parseUrl(Scanner request)
	{
		return request.next();		
	}
	
	/**
	 * Parses the url parameters from the supplied URL
	 * 
	 * @param url the url to parse
	 * @return a HashMap containing the URL parameters
	 * @throws MalformedRequestException
	 * @throws UnsupportedEncodingException
	 */
	HashMap<String, String> parseUrlParams(String url) throws MalformedRequestException, UnsupportedEncodingException
	{
		HashMap<String, String> params = new HashMap<>();
		
		int index = url.indexOf("?");
		if (index != -1)
		{
			String s = url.substring(index + 1);
			
			String[] par = s.split("&");
			for (int i = 0; i < par.length; i++)
			{
				if (!par[i].contains("="))
					throw new MalformedRequestException("Malformed URL Parameter");
				
				String[] key_val = par[i].split("=");
				
				params.put(key_val[0], URLDecoder.decode(key_val[1], ENCODING));
			}
		}
		
		return params;
	}
	
	/**
	 * Parses the post data from the request
	 * 
	 * @param request a Scanner wrapped around a String representation of the request. It is assumed that the headers
	 * have already been scanned
	 * 
	 * @return a HashMap containing the post data
	 * 
	 * @throws MalformedRequestException
	 * @throws UnsupportedEncodingException
	 */
	HashMap<String, String> parsePostData(Scanner request) throws MalformedRequestException, UnsupportedEncodingException
	{
		HashMap<String, String> postData = new HashMap<>();
		
		if (!request.hasNextLine())
			return postData;
		
		String s = request.nextLine();
		while (request.hasNextLine())
			s += request.nextLine();
		
		String[] data = s.split("&");
		for (int i = 0; i < data.length; i++)
		{
			if (!data[i].contains("="))
				throw new MalformedRequestException("Malformed URL Parameter");
			
			String[] key_val = data[i].split("=");
			
			postData.put(key_val[0], URLDecoder.decode(key_val[1], ENCODING));
		}
		
		return postData;
	}
	
	/**
	 * Parses the header information from the request
	 * 
	 * @param request a Scanner wrapped around a string representation of the request. It is assumed that the first line
	 * of the request (the method, url, version) has already been scanned
	 * 
	 * @return a HashMap containing the header information
	 * 
	 * @throws MalformedRequestException
	 * @throws UnsupportedEncodingException
	 */
	HashMap<String, String> parseHeaders(Scanner request) throws MalformedRequestException, UnsupportedEncodingException
	{
		HashMap<String, String> headers = new HashMap<>();
		
		String line = request.nextLine();
		
		while (line != null && !line.isEmpty())
		{
			// Get field
			int index = line.indexOf(":");
			if (index == -1)
				throw new MalformedRequestException("Malfrmed request");
			
			String field = line.substring(0, index);
			
			// Get value
			String content = line.substring(index + 1).trim();
			if (!request.hasNextLine())
			{
				headers.put(field, URLDecoder.decode(content, ENCODING));
				break;
			}
			
			String part = request.nextLine();
			while (part != null && !part.contains(":") && !part.isEmpty())
			{
				content += " " + part.trim();
					
				if (!request.hasNextLine())
					break;
					
				part = request.nextLine();
			}
			
			// Record the header data
			headers.put(field, URLDecoder.decode(content, ENCODING));			 
			
			// move to the next line to process 
			line = part;
		}
		
		return headers;
	}
	
	/**
	 * Verifies that the HTTP version is valid (part of ensuring that the request is properly formatted)
	 * 
	 * @param request a Scanner wrapped around a string representation of the request. It is assumed that only the method
	 * and url have been scanned
	 * 
	 * @return true if the HTTP version is properly formatted, false otherwise
	 */
	boolean verifyHTTPFormat(Scanner request)
	{
		String s = request.nextLine().trim();
		
		return s.matches("HTTP/[0-9]+\\.[0-9]+");
	}

	@Override
	public String getHost() {
		return _headers.get("Host");
	}

	@Override
	public String getUrlPath() {
		return _url;
	}

	@Override
	public String getUrlParameter(String name) {
		return _urlParams.get(name);
	}

	@Override
	public String getHeader(String name) {
		return _headers.get(name);
	}

	@Override
	public InetAddress getClientAddress() {
		return _InetAddress;
	}

	@Override
	public Method getMethod() {
		return _method;
	}

	@Override
	public String getPostParameter(String name) {
		return _postData.get(name);
	}
	
}
