package server.io;

/**
 * Exception for malformed requests. Draws its methods from the Exception class
 * 
 * @author dkirby
 *
 */
public class MalformedRequestException extends Exception {

	private static final long serialVersionUID = -4384565032537136648L;
	
	/**
	 * Constructor 
	 */
	public MalformedRequestException(){
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param string the message for the exception
	 */
	public MalformedRequestException(String message) {
		super(message);
	}
}
