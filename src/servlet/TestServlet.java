package servlet;

import java.io.IOException;
import java.io.OutputStream;

import server.io.HTTPStatusCodes;
import server.io.IRequest;
import server.io.IResponse;

public class TestServlet implements ISimpleServlet {

	@Override
	public void doGet(IRequest request, IResponse response) 
	{
		response.setStatusCode(HTTPStatusCodes.StatusCode.OK.getValue());
		response.setMimeType("text/html");
	
		String header = response.generateHeader();
		
		String body = "<h1>This is a GET request";
		
		OutputStream out = response.getOutputStream();
		
		try
		{
			out.write(header.getBytes());
			out.write(body.getBytes());
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			try 
			{
				out.close();
			} 
			catch (IOException e) 
			{
				System.out.println(e.getMessage());
			}
		}
	}

	@Override
	public void doPost(IRequest request, IResponse response) 
	{
		response.setStatusCode(HTTPStatusCodes.StatusCode.OK.getValue());
		response.setMimeType("text/html");
	
		String header = response.generateHeader();
		
		String body = "<h1>This is a POST request";
		
		OutputStream out = response.getOutputStream();
		
		try
		{
			out.write(header.getBytes());
			out.write(body.getBytes());
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			try 
			{
				out.close();
			} 
			catch (IOException e) 
			{
				System.out.println(e.getMessage());
			}
		}
	}

}
