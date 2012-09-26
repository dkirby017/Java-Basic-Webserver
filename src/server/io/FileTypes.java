package server.io;

/**
 * Maps file extensions to their appropriate Content-Type string
 * 
 * @author dkirby
 *
 */
public class FileTypes 
{	
	/**
	 * Maps the input file extension to the appropriate Content-Type string
	 * 
	 * @param extension the file extension to map
	 * 
	 * @return the appropriate Content-Type string for the input file extension, null if the extension is not supported
	 */
	static public String mapExtension(String extension)
	{
		switch (extension)
		{
			case ".jpg":
			case ".jpeg":
				return "image/jpeg";

			case ".png":
				return "image/png";
				
			case ".gif":
				return "image/gif";
				
			case ".txt":
				return "text/plain";
				
			case ".css":
				return "text/css";
				
			case ".html":
			case ".htm":
				return "text/html";				
		
			default:
				return null;
		}
	}
}
