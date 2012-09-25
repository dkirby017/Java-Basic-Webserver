package server.io;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

public class SimpleResponse_test {

	private SimpleResponse _Response;
	
	@Before
	public void setUp() throws Exception {
		Socket socket = mock(Socket.class);
		
		_Response = new SimpleResponse(socket);
	}

	@Test
	/**
	 * Test that the generated header is in the correct format
	 */
	public void test_generateHeader() {
		
		_Response.setStatusCode(200);		
		_Response.setMimeType("text/plain");
		_Response.setHeader("Foo", "Bar");
		
		String result = _Response.generateHeader();
		String regex = "^HTTP/[0-9]+\\.[0-9]+ [0-9]{3} [a-zA-Z]*[^.]+" +
						"([a-zA-Z]+: [a-zA-Z]+[^.]+){2}" +
						"[^.]+$";
		
		assertTrue(result.matches(regex));		
	}

}
