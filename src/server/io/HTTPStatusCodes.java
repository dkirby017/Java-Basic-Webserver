package server.io;

/**
 * Contains the details for each type of HTTP Status code supported
 * 
 * @author dkirby
 *
 */
public class HTTPStatusCodes {
	
	/**
	 * Enumeration of supported HTTP status codes
	 * 
	 * @author dkirby
	 *
	 */
	public enum StatusCode {
		OK(200),
		Moved_Permenantly(301),
		Bad_Request(400),
		Unauthorized(401),
		Forbidden(403),
		Not_Found(404),
		Internal_Server_Error(500),
		Not_Implemented(501),
		Service_Unavailable(503),
		Invalid(-1);
		
		private final int _code;
		private StatusCode(int code) { _code = code; }
		public int getValue() { return _code; }
	}
	
	/**
	 * Maps an integer status code to a StatusCode enumeration
	 * 
	 * @param statusCode the integer status code to map
	 * 
	 * @return the appropriate StatusCode enumeration value, Invalid if the status code is not supported
	 */
	static public StatusCode mapStatusCode(int statusCode)
	{
		if (statusCode == StatusCode.OK.getValue())
			return StatusCode.OK;
		
		else if (statusCode == StatusCode.Moved_Permenantly.getValue())
			return StatusCode.Moved_Permenantly;
		
		else if (statusCode == StatusCode.Bad_Request.getValue())
			return StatusCode.Bad_Request;
		
		else if (statusCode == StatusCode.Unauthorized.getValue())
			return StatusCode.Unauthorized;
		
		else if (statusCode == StatusCode.Forbidden.getValue())
			return StatusCode.Forbidden;
		
		else if (statusCode == StatusCode.Not_Found.getValue())
			return StatusCode.Not_Found;
		
		else if (statusCode == StatusCode.Internal_Server_Error.getValue())
			return StatusCode.Internal_Server_Error;
		
		else if (statusCode == StatusCode.Not_Implemented.getValue())
			return StatusCode.Not_Implemented;
		
		else if (statusCode == StatusCode.Service_Unavailable.getValue())
			return StatusCode.Service_Unavailable;
		
		else
			return StatusCode.Invalid;
	}
	
	/**
	 * Maps a given status code to the appropriate response string
	 * 
	 * @param code the status code to map
	 * 
	 * @return the appropriate response string for the given status code
	 */
	static public String mapStatusMessage(StatusCode code)
	{
		switch(code)
		{
			case OK:
				return code.getValue() + " OK";
				
			case Moved_Permenantly:
				return code.getValue() + " Moved Permenantly";
				
			case Bad_Request:
				return code.getValue() + " Bad Request";
				
			case Unauthorized:
				return code.getValue() + " Unauthorized";
				
			case Forbidden:
				return code.getValue() + " Forbidden";
				
			case Not_Found:
				return code.getValue() + " Not Found";
				
			case Internal_Server_Error:
				return code.getValue() + " Internal Server Error";
				
			case Not_Implemented:
				return code.getValue() + " Not Implemented";
				
			case Service_Unavailable:
				return code.getValue() + " Service Unavailable";
				
			default:					
				return null;
		}
	}

}
