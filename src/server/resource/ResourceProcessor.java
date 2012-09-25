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
import server.memory.ObjectCache;
import server.processor.IProcessor;

public class ResourceProcessor implements IProcessor {
	
	@Override
	public void process(ClientConnection connection) throws FileNotFoundException, IOException, MalformedRequestException, UnsupportedMethodException {
		
		String url = connection.getRequest().getUrlPath();
		
		// remove any leading '/'
		//if (url.startsWith("/"))
		//	url = url.substring(1);
		
		// remove any url parameters
		if (url.contains("?"))
		{
			int index = url.indexOf("?");
			url = url.substring(0, index);
		}
		
		String extension = "";
		int index = url.lastIndexOf(".");
		if (index != -1)
			extension = url.substring(index);		
		
		ObjectCache cache = ResourceCache.getInstance();
		
		Resource resource = (Resource) cache.get(url);
		
		if (resource == null)
		{
			ResourceFactory factory = new ResourceFactory();
					
			resource = factory.newInstance(url);
			
			cache.cache(url, resource);
		}
		
		outputResource(connection.getResponse(), resource, extension);			
	}

	void outputResource(IResponse response, Resource resource, String extension) throws IOException 
	{
		response.setStatusCode(StatusCode.OK.getValue());
		
		String mimeType = FileTypes.mapExtension(extension);
		if (mimeType != null)
			response.setMimeType(mimeType);
		
		String header = response.generateHeader();
		
		OutputStream output = response.getOutputStream();
		
		output.write(header.getBytes());
		
		output.write(resource.getBytes());
	}
}
