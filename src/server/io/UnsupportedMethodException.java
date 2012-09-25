package server.io;

/**
 * Exception for unsupported request methods. Draws its methods from the Exception class
 * 
 * @author dkirby
 *
 */
public class UnsupportedMethodException extends Exception {

	private static final long serialVersionUID = -1117558151295618856L;
	
	/**
	 * Constructor 
	 */
	public UnsupportedMethodException(){
		super();
	}
	
	/**
	 * Constructor
	 * 
	 * @param string the message for the exception
	 */
	public UnsupportedMethodException(String message) {
		super(message);
	}

}
