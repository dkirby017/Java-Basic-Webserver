package server.processor;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import server.io.ClientConnection;
import server.io.MalformedRequestException;
import server.io.UnsupportedMethodException;

/**
 * Interface for the various request processors
 * 
 * @author dkirby
 *
 */
public interface IProcessor 
{
	/**
	 * Processes the request obtained through the input ClientConnection and generates/sends the appropriate response
	 * to the client
	 * 
	 * @param connection the ClientConnection to get the request and response objects from
	 * 
	 * @throws IOException
	 * @throws MalformedRequestException
	 * @throws UnsupportedMethodException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public void process(ClientConnection connection) throws IOException, MalformedRequestException, UnsupportedMethodException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException, SAXException;
}
