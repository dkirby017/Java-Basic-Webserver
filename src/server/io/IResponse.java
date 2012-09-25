package server.io;

/**
 * Interface for http response objects
 * 
 * @author dkirby
 *
 */
public interface IResponse {

	/**
	 * Set the response HTTP code
	 * 
	 * @param httpStatusCode
	 */
	public void setStatusCode(int httpStatusCode);
	
	/**
	 * Sets the response header to specified value 
	 * 
	 * @param name The parameter name
	 * @param value The parameter value
	 */
	public void setHeader(String name, String value);
	
	/**
	 * Sets the mime type to be returned to the client
	 * 
	 * @param mimeType The mime type to set
	 */
	public void setMimeType(String mimeType);
	
	/**
	 * Generates the header string from the set header values
	 * 
	 * @return a String representing the header data to be transmitted
	 */
	public String generateHeader(); 
	
	/**
	 * Gets the OutputStream that, when written to, will transmit to the client. 
	 * 
	 * @return The output stream
	 */
	public java.io.OutputStream getOutputStream();
}
