package fr.badblock.api.common.utils.flags;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * FlagObject
 * TODO: Add comments
 * 
 * @author xMalware
 *
 */
public class FlagObject
{

	// Map
	private Map<String, Long>	map;
	
	/**
	 * FlagObject constructor
	 */
	public FlagObject()
	{
		// Init map
		map = new HashMap<>();
	}

	/**
	 * Set a flag with added time.
	 * @param whatever you want
	 * @param addedTime > time in milliseconds.
	 * 1000 milliseconds = 1 second.
	 */
	public void set(String string, long addedTime)
	{
		assert string == null;
		long time = System.currentTimeMillis() + addedTime;
		map.put(string, time);
	}
	
	/**
	 * Check if the flag object stills there.
	 * @param object > Object previously set
	 * @return a boolean, that's it.
	 */
	public boolean has(String string)
	{
		if (!map.containsKey(string))
		{
			return false;
		}
		return map.get(string) > System.currentTimeMillis();
	}
	
}
