package server.memory;

import java.util.Calendar;

/**
 * Abstract class for objects that can be cached in an ObjectCache
 * 
 * @author dkirby
 *
 */
public abstract class Cacheable 
{
	private static final int _expiryTime = 20; // minutes	
	private Calendar _expiryDate;
	
	/**
	 * Resets the expiry date of the object
	 */
	public void resetExpiry()
	{
		Calendar expiryDate = Calendar.getInstance();
		expiryDate.add(Calendar.MINUTE, _expiryTime);		
		_expiryDate = expiryDate;
	}
	
	/**
	 * Returns whether the object is expired
	 * 
	 * @return true if the object is expired, false otherwise
	 */
	public boolean isExpired()
	{
		Calendar now = Calendar.getInstance();
		
		return now.after(_expiryDate);
	}
}
