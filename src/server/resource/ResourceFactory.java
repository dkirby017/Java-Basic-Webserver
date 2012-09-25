package server.resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ResourceFactory 
{
	public ResourceFactory()
	{		
	}
	
	public Resource newInstance(String url) throws FileNotFoundException, IOException
	{
		// Check if the requested file exists
		File filePath = new File(System.getProperty("user.dir") + url);	
		if (!filePath.exists())
			throw new FileNotFoundException("File not found: " + url);
												
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(filePath));
		
		try
		{	
			int count = input.available();
			byte[] bytes = new byte[count];
			
			input.read(bytes);
			
			Resource resource = new Resource(bytes);
			
			return resource;
		}
		finally
		{
			input.close();
		}
	}
}
