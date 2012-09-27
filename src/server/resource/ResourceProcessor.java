package server.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import server.io.ClientConnection;
import server.io.FileTypes;
import server.io.IResponse;
import server.io.MalformedRequestException;
import server.io.UnsupportedMethodException;
import server.io.HTTPStatusCodes.StatusCode;
import server.processor.IProcessor;

/**
 * Specialised processor for static resources. Processes the request for a static resource and generates/sends the response 
 * to the client for that resource. If any errors occur, the exceptions are passed up the call stack
 * 
 * @author dkirby
 *
 */
public class ResourceProcessor implements IProcessor {
	
	@Override
	public void process(ClientConnection connection) throws FileNotFoundException, IOException, MalformedRequestException, UnsupportedMethodException {
		
		String url = connection.getRequest().getUrlPath();
		
		// remove any url parameters
		if (url.contains("?"))
		{
			int index = url.indexOf("?");
			url = url.substring(0, index);
		}
		
		// get the file extension
		String extension = getExtension(url);
				
		
		// get the ResourceCache
		ResourceCache cache = getResourceCache();
		
		// attempt to get the resource from the cache
		CacheableResource resource = (CacheableResource) cache.get(url);
		
		// if it hasn't been cached, create the resource and cache it
		if (resource == null)
		{
			ResourceFactory factory = getResourceFactory();
					
			resource = factory.newInstance(url);
			
			cache.cache(url, resource);
		}
		
		// output the resource to the client
		outputResource(connection.getResponse(), resource, extension);			
	}
	
	/**
	 * Extracts the file extension from the url
	 * 
	 * @param url the url of the request
	 * 
	 * @return the file extension from the url, or an empty string if no extension is present
	 */
	String getExtension(String url)
	{
		int index = url.lastIndexOf(".");
		
		if (index != -1)
			return url.substring(index);
		else
			return "";
	}
	
	/**
	 * Gets a new resource factory
	 * 
	 * @return a new ResourceFactory object
	 */
	ResourceFactory getResourceFactory()
	{
		return new ResourceFactory();
	}
	
	/**
	 * Gets the resource cache
	 * 
	 * @return the resource cache
	 */
	ResourceCache getResourceCache()
	{
		return ResourceCache.getInstance();
	}

	/**
	 * Outputs the response for the resource to the client
	 * 
	 * @param response the client's response object
	 * @param resource the resource to send
	 * @param extension the file extension of the resource
	 * 
	 * @throws IOException if an I/O error occurs
	 */
	void outputResource(IResponse response, CacheableResource resource, String extension) throws IOException 
	{
		// set the status code
		response.setStatusCode(StatusCode.OK.getValue());
		
		// set the mime type
		String mimeType = FileTypes.mapExtension(extension);
		if (mimeType != null)
			response.setMimeType(mimeType);
		
		// generate the header
		String header = response.generateHeader();
		
		// get the output stream to write to
		OutputStream output = response.getOutputStream();
		
		// write the header to the client
		output.write(header.getBytes());
		
		// write the resource to the client
		output.write(resource.getBytes());
	}
}
