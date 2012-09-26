package server.io;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import server.io.RequestMethods.Method;

/**
 * Tests the SimpleRequest class
 * 
 * @author dkirby
 *
 */
public class SimpleRequest_test {

	/** 
	 * Local tester subclass. Overrides the parseInput method so that I can test the individual parsing
	 * methods when I want to, rather that running them during construction
	 * 
	 * @author dkirby
	 *
	 */
	private class SimpleRequest_tester extends SimpleRequest
	{		
		public SimpleRequest_tester(Socket socket) throws IOException, MalformedRequestException, UnsupportedMethodException {
			super(socket);
		}

		@Override
		void parseInput(InputStream input) throws IOException, MalformedRequestException, UnsupportedMethodException {			
		}
		
		void runParseInput(InputStream input) throws IOException, MalformedRequestException, UnsupportedMethodException {
			super.parseInput(input);
		}
	}
	
	private SimpleRequest_tester _Request;
	
	@Before
	public void setUp() throws Exception {
		Socket socket = mock(Socket.class);
						
		_Request = spy(new SimpleRequest_tester(socket));
	}

	@Test
	/**
	 * Test that parseMethod() returns the expected result 
	 */
	public void test_parseMethod() {
		String method = "GET";
		
		try 
		{
			Method result = _Request.parseMethod(method);
			
			assertEquals(Method.GET, result);
		} 
		catch (Exception e) 
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that parseMethod() throws a UnsupportedMethodException when expected
	 */
	public void test_parseMethod_error() {
		String header = "NOT_A_METHOD";
		
		try 
		{
			_Request.parseMethod(header);
			
			fail();
		} 
		catch (UnsupportedMethodException e) 
		{
			// pass
		}
		catch (Exception e)
		{
			fail();
		}
	}
		
	@Test
	/**
	 * Test that parseUrlParams() returns the expected result
	 */
	public void test_parseUrlParams() {
		String url = "/foo/bar?one=two&three=four&five=six+seven";		
		
		try 
		{
			HashMap<String, String> result = _Request.parseUrlParams(url);
			
			assertEquals("two", result.get("one"));
			assertEquals("four", result.get("three"));
			assertEquals("six seven", result.get("five"));
		} 
		catch (Exception e) 
		{
			fail();
		}		
	}
	
	@Test
	/**
	 * Test that parseUrlParams() throws a MalformedRequestException when expected
	 */
	public void test_parseUrlParams_error() {
		String url = "/foo/bar?one=two&three=four&five";		
		
		try 
		{
			_Request.parseUrlParams(url);
			
			fail();
		} 
		catch (MalformedRequestException e)
		{
			// pass
		}
		catch (Exception e) 
		{
			fail();
		}		
	}
	
	@Test
	/**
	 * Test that parsePostData() returns the expected results
	 */
	public void test_parsePostData() {
		String header = "one=two&three=four&five=six+\n" +
						"seven\n" +
						"\n";
		
		Scanner scn = new Scanner(header);
		
		try 
		{
			HashMap<String, String> result = _Request.parsePostData(scn);
			
			assertEquals("two", result.get("one"));
			assertEquals("four", result.get("three"));
			assertEquals("six seven", result.get("five"));
		} 
		catch (Exception e) 
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that parsePostData() throws a MalformedRequestException when expected
	 */
	public void test_parsePostData_error() {
		String header =	"one=two&three&five=six+\n" +
						"seven\n" +
						"\n";
		
		Scanner scn = new Scanner(header);
		
		try 
		{
			_Request.parsePostData(scn);
			
			fail();
		} 
		catch (MalformedRequestException e)
		{
			// pass
		}
		catch (Exception e) 
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that parseHeaders() returns the expected result
	 */
	public void test_parseHeaders() {
		String header = "Content-Type: text/plain\n" +
						"Host: localhost\n" +
						"Foo: bar\n" +
						" piece\n" +
						"\n" +
						"one=two&three=four&five=six+\n" +
						"seven";
		
		Scanner scn = new Scanner(header);
		
		try 
		{
			HashMap<String, String> result = _Request.parseHeaders(scn);
			
			assertEquals("text/plain", result.get("Content-Type"));
			assertEquals("localhost", result.get("Host"));
			assertEquals("bar piece", result.get("Foo"));
			assertTrue(scn.hasNextLine()); // ensure that it did not read to the end of the request
		} 
		catch (Exception e) 
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that parseHeaders() returns the expected result
	 */
	public void test_parseHeaders_error() {
		String header = "Invalid Line\n" +
						"Content-Type: text/plain\n" +
						"Host: localhost\n" +
						"Foo: bar\n" +
						" piece\n" +
						"\n" +
						"one=two&three=four&five=six+\n" +
						"seven";
		
		Scanner scn = new Scanner(header);
		
		try 
		{
			_Request.parseHeaders(scn);
			
			fail();
		} 
		catch (MalformedRequestException e)
		{
			// pass
		}
		catch (Exception e) 
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Test that verifyHTTPFormat() returns the expected result
	 */
	public void test_verifyHTTPFormat() {
		String header = "HTTP/1.1";
		
		assertTrue(_Request.verifyHTTPFormat(header));
		
		header = "piece";
		
		assertFalse(_Request.verifyHTTPFormat(header));
	}
	
	@Test
	/**
	 * Tests that parseInput() parses the header in the correct order
	 */
	public void test_parseInput() {
		String header = "POST /foo/bar?name=foo&type=bar HTTP/1.1\n" +				
				"Content-Type: text/plain\n" +
				"Host: localhost\n" +
				"Foo: bar\n" +
				" piece\n" +
				"\n" +
				"one=two&three=four&five=six+\n" +
				"seven\r\n" +
				"\n";
		
		InputStream input = mock(InputStream.class);
		
		try 
		{
			doReturn(header).when(_Request).readInputStream(input);
			
			_Request.runParseInput(input);
			
			assertEquals(Method.POST, _Request.getMethod());
			assertEquals("/foo/bar?name=foo&type=bar", _Request.getUrlPath());
			assertEquals("foo", _Request.getUrlParameter("name"));
			assertEquals("bar", _Request.getUrlParameter("type"));
			assertEquals("text/plain", _Request.getHeader("Content-Type"));
			assertEquals("localhost", _Request.getHost());
			assertEquals("bar piece", _Request.getHeader("Foo"));
			assertEquals("two", _Request.getPostParameter("one"));
			assertEquals("four", _Request.getPostParameter("three"));
			assertEquals("six seven", _Request.getPostParameter("five"));
		}
		catch (Exception e)
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Tests that parseInput() passes the exceptions up the call stack as expected
	 */
	public void test_parseInput_error() {
		String header = "POST /foo/bar?name=foo&type=bar HTTP/1.1\n" +				
				"Content-Type: text/plain\n" +
				"Host: localhost\n" +
				"Foo: bar\n" +
				" piece\n" +
				"\n" +
				"one=two&three=four&five=six+\n" +
				"seven\r\n" +
				"\n";
		
		InputStream input = mock(InputStream.class);
		
		try 
		{
			doThrow(new IOException()).when(_Request).readInputStream(input);
			try
			{
				_Request.runParseInput(input);
				fail();
			} 
			catch(IOException e)
			{
				// pass
			}
			catch (Exception e)
			{
				fail();
			}
			
			doReturn(header).when(_Request).readInputStream(input);
			doThrow(new UnsupportedMethodException()).when(_Request).parseMethod("POST");
			try
			{
				_Request.runParseInput(input);
				fail();
			} 
			catch(UnsupportedMethodException e)
			{
				// pass
			}
			catch (Exception e)
			{
				fail();
			}
			
			doReturn(Method.POST).when(_Request).parseMethod("POST");
			doReturn(false).when(_Request).verifyHTTPFormat("HTTP/1.1");
			try
			{
				_Request.runParseInput(input);
				fail();
			} 
			catch(MalformedRequestException e)
			{
				// pass
			}
			catch (Exception e)
			{
				fail();
			}
		}
		catch (Exception e)
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Tests that readInputStream obtains reads in the data correctly
	 */
	public void test_readInputStream()
	{
		try
		{
			String header = "POST /foo/bar?name=foo&type=bar HTTP/1.1\r\n" +				
							"Content-Type: text/plain\r\n" +
							"Host: localhost\r\n" +
							"Foo: bar\r\n" +
							" piece\r\n" +
							"\r\n" +
							"one=two&three=four&five=six+\r\n" +
							"seven\r\n" +
							"\r\n";
			
			// build inputstream around the string above
			InputStream in = new ByteArrayInputStream(header.getBytes("UTF-8"));
			
			// read from the inputstream
			String result = _Request.readInputStream(in);
			
			// replace the \r\n by a single new line and remove and leading/trailing whitespace so
			// that it can match the expected behaviour of readInputStream()
			header = header.replace("\r\n", "\n").trim();
			
			assertEquals(header, result);
		}
		catch (Exception e)
		{
			fail();
		}
	}
	
	@Test
	/**
	 * Tests that readInputStream passes the IOException up the call stack as expected
	 */
	public void test_readInputStream_error()
	{
		try
		{			
			// build mock inputstream
			InputStream in = mock(InputStream.class);
			
			// read from the inputstream
			_Request.readInputStream(in);
			
			fail();
		}
		catch (IOException e)
		{
			// pass
		}
		catch (Exception e)
		{
			fail();
		}
	}

}
