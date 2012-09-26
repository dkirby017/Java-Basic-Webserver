package server.memory;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ThreadManager_test {

	private ThreadManager _Manager;
	
	@Before
	public void setUp() throws Exception {
		_Manager = new ThreadManager(10);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_notifyThreadFinished() {
		
		_Manager.notifyThreadFinished();
		assertEquals(11, _Manager.getFreeThreadCount());
		
		_Manager.notifyThreadFinished();
		assertEquals(12, _Manager.getFreeThreadCount());
	}
	
	@Test
	public void test_waitForFreeThread() {
		try 
		{
			_Manager.waitForFreeThread();
			assertEquals(9, _Manager.getFreeThreadCount());
			
			_Manager.waitForFreeThread();
			assertEquals(8, _Manager.getFreeThreadCount());
		} 
		catch (Exception e) 
		{
			fail();
		}
	}

}
