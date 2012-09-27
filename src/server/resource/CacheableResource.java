package server.resource;

import server.memory.Cacheable;

/**
 * A container for the static resources. Contains the bytes for the resource so that it can be cached
 * 
 * @author dkirby
 *
 */
public class CacheableResource extends Cacheable
{	
	private byte[] _bytes;
	
	/**
	 * Constructor
	 * 
	 * @param bytes the bytes for the resource
	 */
	public CacheableResource(byte[] bytes)
	{
		_bytes = bytes;
		
		resetExpiry();
	}
	
	/**
	 * Gets the bytes for the resource
	 * 
	 * @return the resource's bytes
	 */
	public byte[] getBytes()
	{
		resetExpiry();
		
		return _bytes;
	}	
}
