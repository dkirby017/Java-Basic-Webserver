package server.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import server.io.ClientConnection;
import server.io.IErrorLogger;
import server.io.IResponse;
import server.io.MalformedRequestException;
import server.io.UnsupportedMethodException;
import server.io.HTTPStatusCodes.StatusCode;
import server.memory.ThreadManager;
import server.resource.ResourceProcessor;
import server.servlet.ServletManager;
import server.servlet.ServletProcessor;

/**
 * Performs initial processing of the client's request, determining which specialized processor to pass the request off to.
 * 
 * Also handles outputting all of the error messages to the client
 * 
 * @author dkirby
 *
 */
public class RequestProcessor extends Thread implements IProcessor{

	private ThreadManager _ThreadManager;
	
	private ClientConnection _connection;
	private IErrorLogger _logger;
	
	/**
	 * Constructor
	 * 
	 * @param manager the ThreadManager object to signal when the processing is complete
	 * @param connection the ClientConnection for the client's request/response
	 * @param logger the Error Logger to use to log any error messages
	 */
	public RequestProcessor(ThreadManager manager, ClientConnection connection, IErrorLogger logger)
	{
		_ThreadManager = manager;
		
		_connection = connection;
		_logger = logger;
	}
	
	@Override
	public void run() 
	{
		process(_connection);
	}
	
	/**
	 * Performs initial processing of the client's request and passes the ClientConnection to the appropriate specialized
	 * processor. Handles all exceptions from processing and outputs the appropriate error response to the client.
	 * 
	 * @param connection the ClientConnection for the client's request/response
	 */
	public void process(ClientConnection connection) 
	{
		ServletManager servletManager = ServletManager.getInstance();
		
		try
		{
			IProcessor processor = null;
			
			// determine whether the request is for a servlet
			if (servletManager.isServletRequest(connection.getRequest().getUrlPath()))
			{
				// pass to servlet processor
				processor = new ServletProcessor();						
			}
			else 
			{
				// pass to static resource processor
				processor = new ResourceProcessor();
			}
			
			processor.process(connection);
		}
		catch (UnsupportedMethodException e)
		{
			sendErrorResponse(connection, StatusCode.Not_Implemented);
			_logger.log(e.getMessage());	
		}								
		catch (MalformedRequestException e) 
		{
			sendErrorResponse(connection, StatusCode.Bad_Request);
			_logger.log(e.getMessage());					
		}
		catch (FileNotFoundException | ClassNotFoundException e)
		{
			sendErrorResponse(connection, StatusCode.Not_Found);
			_logger.log(e.getMessage());
		}
		catch (ParserConfigurationException | SAXException | IOException | InstantiationException | IllegalAccessException e) 
		{
			sendErrorResponse(connection, StatusCode.Internal_Server_Error);
			_logger.log(e.getMessage());	
		} 
		finally
		{
			try 
			{
				// attempt to close the connection
				if (!connection.isClosed())							
					connection.close();
			}
			catch (IOException e)
			{
				_logger.log(e.toString());
			}
			
			// signal that the processing is finished
			_ThreadManager.notifyThreadFinished();
		}
	}
	
	/**
	 * Sends the error response to the client
	 * 
	 * @param connection the ClientConnection for the client's response
	 * @param code the StatusCode for the response
	 */
	void sendErrorResponse(ClientConnection connection, StatusCode code)
	{
		try
		{
			// get the response
			IResponse response = connection.getResponse();
			
			// set the response status code
			response.setStatusCode(code.getValue());
			
			// get the outputstream to write to
			OutputStream outputStream = response.getOutputStream();
			
			// generate the http header and write the response to the client
			outputStream.write(response.generateHeader().getBytes());
		}
		catch (IOException ex)
		{
			_logger.log(ex.getMessage());
		}
	}
}
