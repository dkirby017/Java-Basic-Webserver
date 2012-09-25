package server.io;

import java.util.LinkedList;
import java.util.NoSuchElementException;


/**
 * 
 * @author dkirby
 * 
 * Monitor: Maintains a FIFO list of connections waiting to be processed 
 *
 */
public class ConnectionManager {
	
	private LinkedList<ClientConnection> _connections;
	
	/**
	 * Constructor
	 */
	public ConnectionManager()
	{
		_connections = new LinkedList<ClientConnection>();
	}
	
	/**
	 * Adds the specified connection to the end of the list
	 * 
	 * @param task the connection to add
	 */
	public synchronized void push(ClientConnection connection)
	{
		_connections.addLast(connection);
		
		this.notify();
	}
	
	/**
	 * Pops the front connection off the list
	 * 
	 * @return the front connection from the list, null if the list is empty
	 * @throws NoSuchElementException is pop() is called on an empty list
	 * @throws InterruptedException 
	 */
	public synchronized ClientConnection pop() throws NoSuchElementException, InterruptedException
	{	
		while (isEmpty())
		{
			this.wait();
		}
		
		return _connections.pop();
	}
	
	/**
	 * Returns the front connection from the list without modifying the list
	 * 
	 * @return the front connection from the list
	 */
	public ClientConnection peek()
	{
		return _connections.peek();
	}
	
	/**
	 * Returns the connection at the specified index without modifying the list.
	 * The indices start at 0.
	 * 
	 * @param index the index of the connection to get
	 * 
	 * @return the connection at the specified index
	 */
	public ClientConnection peek_at(int index) throws IndexOutOfBoundsException
	{
		return _connections.get(index);
	}
	
	/**
	 * Returns whether the list is empty
	 * 
	 * @return true if the list is empty, otherwise returns false
	 */
	public boolean isEmpty() 
	{
		return _connections.isEmpty();
	}
	
	/**
	 * Removes all objects from the connection list
	 */
	public synchronized void clear()
	{
		_connections.clear();
	}
	
}