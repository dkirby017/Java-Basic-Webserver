package server.memory;

/**
 * Implements a semaphore to manage the number of active threads
 * 
 * @author dkirby
 *
 */
public class ThreadManager 
{
	private int _freeThreads;
	
	/**
	 * Constructor
	 * 
	 * @param maxThreads the maximum number of threads to allow simultaneously
	 */
	public ThreadManager(int maxThreads)
	{
		_freeThreads = maxThreads;
	}
	
	/**
	 * Indicates that a thread has finished. Increments the number of available threads and notifies any waiting
	 * waitForFreeThread() processes
	 */
	public synchronized void notifyThreadFinished()
	{
		_freeThreads++;
		
		this.notify();
	}
	
	/**
	 * Attempts to decrement the number of free threads, waiting until the value is at least 1 before doing so.
	 * 
	 * @throws InterruptedException if an interruption occurs while waiting
	 */
	public synchronized void waitForFreeThread() throws InterruptedException
	{
		while (_freeThreads == 0)
			this.wait();
		
		_freeThreads--;
	}
	
	/**
	 * Gets the number of free threads
	 * 
	 * @return the number of free threads
	 */
	public int getFreeThreadCount()
	{
		return _freeThreads;
	}
}
