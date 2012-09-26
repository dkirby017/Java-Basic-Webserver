package server.io;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.io.ClientConnection;
import server.io.ConnectionManager;

/**
 * Test the ConnectionManager class
 * 
 * @author dkirby
 *
 */
public class ConnectionManager_test {

	private ConnectionManager _connectionManager;
	
	@Before
	public void setUp() {
		_connectionManager = new ConnectionManager();
	}

	@After
	public void tearDown() {		
		_connectionManager.clear();
	}

	@Test
	/**
	 * Test that objects pushed into the list are added in the correct order
	 */
	public void test_pushTask() {
		ClientConnection con1 = mock(ClientConnection.class);
		ClientConnection con2 = mock(ClientConnection.class);
		
		// ensure list is empty to begin with
		assertTrue(_connectionManager.isEmpty());
		
		_connectionManager.push(con1);
		
		// First object should be at the front of the list
		assertSame(con1, _connectionManager.peek());

		_connectionManager.push(con2);
		
		// First object should still be at the front of the list
		assertSame(con1, _connectionManager.peek());
		
		// Second object should be at the second location (index 1)
		assertSame(con2, _connectionManager.peek_at(1));
	}	

	@Test
	/**
	 * Test that objects popped off the list are popped in the correct order, and that
	 * the order of the remaining objects is unchanged
	 */
	public void test_popTask() throws NoSuchElementException, InterruptedException {
		
		// ensure list is empty to begin with
		assertTrue(_connectionManager.isEmpty());
		
		ClientConnection con1 = mock(ClientConnection.class);
		ClientConnection con2 = mock(ClientConnection.class);
		
		// push two objects onto the list
		_connectionManager.push(con1);		
		_connectionManager.push(con2);
		
		ClientConnection result = _connectionManager.pop();
		
		// the popped object should be the first object added to the list
		assertSame(con1, result);
		
		// front object should now be the second object added to the list
		assertSame(con2, _connectionManager.peek());
		
		result = _connectionManager.pop();
		
		// the popped object should be the second object added to the list
		assertSame(con2, result);
		
		// list should now be empty
		assertTrue(_connectionManager.isEmpty());
	}
	
	@Test
	/**
	 * Test that peek_at() throws an exception if the index is out of bounds
	 */
	public void test_peek_at()
	{
		try 
		{
			_connectionManager.peek_at(-1);
			
			fail();
		} 
		catch (IndexOutOfBoundsException e) 
		{
			// pass
		}
		catch (Exception e)
		{
			fail();
		}
	}

}
