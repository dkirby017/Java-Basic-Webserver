package server.io;

/**
 * Interface for error logging classes
 * 
 * @author dkirby
 *
 */
public interface IErrorLogger {

	/**
	 * Log the specified error message. 
	 * 
	 * @param message the error message to log
	 * 
	 * @return true if the log was successful, false if it fails
	 */
	public boolean log(String message);
	
	/**
	 * Format the error message
	 * 
	 * @param message the error message to format
	 * 
	 * @return the formatted error message
	 */
	String formatMessage(String message);
	
}
