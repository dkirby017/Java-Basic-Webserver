package server.memory;

public class ThreadManager 
{
	private int _freeThreads;
	
	public ThreadManager(int maxThreads)
	{
		_freeThreads = maxThreads;
	}
	
	public synchronized void notifyThreadFinished()
	{
		_freeThreads++;
		
		this.notify();
	}
	
	public synchronized void waitForFreeThread() throws InterruptedException
	{
		while (_freeThreads == 0)
			this.wait();
		
		_freeThreads--;
	}
}
