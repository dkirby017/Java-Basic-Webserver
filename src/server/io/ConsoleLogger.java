package server.io;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author dkirby
 *
 * Implementation of the IErrorLogger interface. Logs errors to the console stdout and stderr
 *
 */
public class ConsoleLogger implements IErrorLogger {

	@Override
	public boolean log(String message) {
		String formattedMessage = formatMessage(message);
		
		System.out.print(formattedMessage);
		System.err.print(formattedMessage);
		
		return true;
	}

	@Override
	public String formatMessage(String message) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		
		return dateFormat.format(date) + ": Error: " + message + "\n";
	}

}
