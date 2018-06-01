package fr.badblock.api.common.utils.general;

import java.util.HashMap;

/**
 * GlobalFlags class
 * It's very useful to all projets (plugins/apps) to set a flag time to an object.
 * For example, to prevent spamming, you can flag the main character object with a time
 * 
 * @author Aurelian Motiz
 */
public class GlobalFlags {

	// Map where all objects are stored
	private static HashMap<Object, Long> map = new HashMap<>();
	
	public static void main(String[] args)
	{
		set(null, 1000);
	}
	
	/**
	 * Set a flag object with added time.
	 * @param object > object, whatever you want
	 * @param addedTime > time in milliseconds.
	 * 1000 milliseconds = 1 second.
	 */
	public static void set(Object object, long addedTime)
	{
		assert object == null;
		long time = System.currentTimeMillis() + addedTime;
		map.put(object, time);
	}
	
	/**
	 * Check if the flag object stills there.
	 * @param object > Object previously set
	 * @return a boolean, that's it.
	 */
	public static boolean has(Object object)
	{
		if (!map.containsKey(object))
		{
			return false;
		}
		return map.get(object) > System.currentTimeMillis();
	}
	
}
