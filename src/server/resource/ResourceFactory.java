package server.resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A factory that generates resource objects from a given url string
 * 
 * @author dkirby
 *
 */
public class ResourceFactory 
{
	/**
	 * Constructor
	 */
	public ResourceFactory()
	{		
	}
	
	/**
	 * Generates a new resource object from the given url string
	 * 
	 * @param url the url string for the resource
	 * 
	 * @return a resource object for the specified resource
	 * 
	 * @throws FileNotFoundException if the file does not exist
	 * @throws IOException if an I/O error occurs
	 */
	public Resource newInstance(String url) throws FileNotFoundException, IOException
	{
		File filePath = getFile(url);
												
		return buildNewResource(filePath);
	}
	
	/**
	 * Returns a new File object for the specified URL
	 * 
	 * @param url the file path URL
	 * 
	 * @return a File object for the specified URL
	 * @throws FileNotFoundException if the file does not exist
	 */
	File getFile(String url) throws FileNotFoundException
	{
		// Check if the requested file exists
		File filePath = new File(System.getProperty("user.dir") + url);	
		if (!filePath.exists())
			throw new FileNotFoundException("File not found: " + url);
		
		return filePath;
	}
	
	/**
	 * Builds a new Resource object from a given File object
	 * 
	 * @param filePath the File to build the resource from
	 * 
	 * @return a new Resource object containing the bytes from the file
	 * 
	 * @throws IOException if an I/O error occurs while reading the file
	 */
	Resource buildNewResource(File filePath) throws IOException
	{
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(filePath));
		
		try
		{
			// read the bytes of the resource
			int count = input.available();
			byte[] bytes = new byte[count];
			
			input.read(bytes);
			
			Resource resource = new Resource(bytes);
			
			return resource;
		}
		finally
		{
			// close the file
			input.close();
		}
	}
}
