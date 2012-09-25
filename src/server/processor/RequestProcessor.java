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

public class RequestProcessor extends Thread {

	private ThreadManager _ThreadManager;
	
	private ClientConnection _connection;
	private IErrorLogger _logger;
	
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
	
	public void process(ClientConnection connection) 
	{
		ServletManager servletManager = ServletManager.getInstance();
		
		try
		{
			IProcessor processor = null;
			
			
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
				if (!connection.isClosed())							
					connection.close();
			}
			catch (IOException e)
			{
				_logger.log(e.toString());
			}
			
			_ThreadManager.notifyThreadFinished();
		}
	}
	
	void sendErrorResponse(ClientConnection connection, StatusCode code)
	{
		try
		{
			IResponse response = connection.getResponse();
			
			response.setStatusCode(code.getValue());
			OutputStream outputStream = response.getOutputStream();
			
			outputStream.write(response.generateHeader().getBytes());
		}
		catch (IOException ex)
		{
			_logger.log(ex.getMessage());
		}
	}
}
