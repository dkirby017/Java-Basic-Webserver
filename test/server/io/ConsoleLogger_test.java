package server.io;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the ConsoleLogger class
 * 
 * @author dkirby
 *
 */
public class ConsoleLogger_test {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private ConsoleLogger _ConsoleLogger;

	@Before
	public void setUp() {
		/**
		 * Set System.out and System.err to write to byte array output streams so that 
		 * the output can be tested
		 */
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	    
	    _ConsoleLogger = spy(new ConsoleLogger());
	}

	@After
	public void cleanUp() {
	    System.setOut(null);
	    System.setErr(null);
	}

	@Test
	/**
	 * Test that the log method outputs the message as expected
	 */
	public void test_log() {
		
		String message = "Test message";
		
		/**
		 * Mock out the formatMessage method so that log() can be tested in isolation
		 */
		doReturn(message).when(_ConsoleLogger).formatMessage(message);
		
		_ConsoleLogger.log(message);
		
		assertEquals(message, outContent.toString());
		assertEquals(message, errContent.toString());
	}

}
