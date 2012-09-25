package server.io;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

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
		String header = "GET /foo/bar HTTP/1.1\n" +
						"Content-Type: text/plain\n" +
						"";
		
		Scanner scn = new Scanner(header);
		
		try 
		{
			Method result = _Request.parseMethod(scn);
			
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
		String header = "INVALID /foo/bar HTTP/1.1\n" +
						"Content-Type: text/plain\n" +
						"";
		
		Scanner scn = new Scanner(header);
		
		try 
		{
			_Request.parseMethod(scn);
			
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
	 * Test that parseUrl() returns the expected result
	 */
	public void test_parseUrl() {
		String url = "/foo/bar?one=two&three=four&five=six+seven";
		String header = "GET " + url + " HTTP/1.1\n" +
						"Content-Type: text/plain\n" +
						"";

		Scanner scn = new Scanner(header);
		scn.next(); // skip the GET since it would already have been scanned
		
		String result = _Request.parseUrl(scn);
			
		assertEquals(url, result);		
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
		String header = "POST /foo/bar HTTP/1.1\n" +
						"Content-Type: text/plain\n" +
						"\n" +
						"one=two&three=four&five=six+\n" +
						"seven";
		
		Scanner scn = new Scanner(header);
		scn.nextLine(); // skip the header section that would have already been scanned
		scn.nextLine();
		scn.nextLine();
		
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
		String header = "POST /foo/bar HTTP/1.1\n" +
						"Content-Type: text/plain\n" +
						"\n" +
						"one=two&three&five=six+\n" +
						"seven";
		
		Scanner scn = new Scanner(header);
		scn.nextLine(); // skip the header section that would have already been scanned
		scn.nextLine();
		scn.nextLine();
		
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
		String header = "POST /foo/bar HTTP/1.1\n" +
						"Content-Type: text/plain\n" +
						"Host: localhost\n" +
						"Foo: bar\n" +
						" piece\n" +
						"\n" +
						"one=two&three=four&five=six+\n" +
						"seven";
		
		Scanner scn = new Scanner(header);
		scn.nextLine(); // skip the first line that would have already been scanned
		
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
		String header = "POST /foo/bar HTTP/1.1\n" +
						"Invalid Line\n" +
						"Content-Type: text/plain\n" +
						"Host: localhost\n" +
						"Foo: bar\n" +
						" piece\n" +
						"\n" +
						"one=two&three=four&five=six+\n" +
						"seven";
		
		Scanner scn = new Scanner(header);
		scn.nextLine(); // skip the first line that would have already been scanned
		
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
		String header = "POST /foo/bar HTTP/1.1";
		
		Scanner scn = new Scanner(header);
		scn.next(); // skip the first two tokens (they would have already been scanned)
		scn.next();
		
		assertTrue(_Request.verifyHTTPFormat(scn));
		
		header = "POST /foo/bar piece HTTP/1.1";
		scn = new Scanner(header);
		scn.next();
		scn.next();
		
		assertFalse(_Request.verifyHTTPFormat(scn));
		
		header = "POST /foo/bar Invalid";
		scn = new Scanner(header);
		scn.next();
		scn.next();
		
		assertFalse(_Request.verifyHTTPFormat(scn));
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
				"seven";
		
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

}
