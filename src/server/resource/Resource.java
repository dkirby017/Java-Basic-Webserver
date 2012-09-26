package server.resource;

/**
 * A container for the static resources. Contains the bytes for the resource so that it can be cached
 * 
 * @author dkirby
 *
 */
public class Resource 
{
	private byte[] _bytes;
	
	/**
	 * Constructor
	 * 
	 * @param bytes the bytes for the resource
	 */
	public Resource(byte[] bytes)
	{
		_bytes = bytes;
	}
	
	/**
	 * Gets the bytes for the resource
	 * 
	 * @return the resource's bytes
	 */
	public byte[] getBytes()
	{
		return _bytes;
	}
}
