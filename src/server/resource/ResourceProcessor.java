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
		String extension = "";
		int index = url.lastIndexOf(".");
		if (index != -1)
			extension = url.substring(index);		
		
		// get the ResourceCache
		ResourceCache cache = ResourceCache.getInstance();
		
		// attempt to get the resource from the cache
		Resource resource = (Resource) cache.get(url);
		
		// if it hasn't been cached, create the resource and cache it
		if (resource == null)
		{
			ResourceFactory factory = new ResourceFactory();
					
			resource = factory.newInstance(url);
			
			cache.cache(url, resource);
		}
		
		// output the resource to the client
		outputResource(connection.getResponse(), resource, extension);			
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
	void outputResource(IResponse response, Resource resource, String extension) throws IOException 
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
