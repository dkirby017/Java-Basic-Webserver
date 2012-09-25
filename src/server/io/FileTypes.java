package server.io;

public class FileTypes 
{	
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
