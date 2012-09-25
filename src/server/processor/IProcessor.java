package server.processor;

import java.io.IOException;

import server.io.ClientConnection;
import server.io.MalformedRequestException;
import server.io.UnsupportedMethodException;

public interface IProcessor 
{
	public void process(ClientConnection connection) throws IOException, MalformedRequestException, UnsupportedMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException;
}
