package server.resource;

public class Resource 
{
	private byte[] _bytes;
	
	public Resource(byte[] bytes)
	{
		_bytes = bytes;
	}
	
	public byte[] getBytes()
	{
		return _bytes;
	}
}
