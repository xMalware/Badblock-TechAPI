package fr.badblock.api.common.sync.bungee;

import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;

/**
 * 
 * Bungee Packet Processing Abstract Class
 * 
 * @author xMalware
 *
 */
public abstract class _BungeeProcessing {

	/**
	 * Constructor
	 */
	public _BungeeProcessing()
	{
		// Set process
		getPacketType().setProcess(this);
	}
	
	/**
	 * Message processing
	 * 
	 * @param message
	 */
	public abstract void done(String message);
	
	/**
	 * Get packet type
	 * 
	 * @return Returns the bungee packet type
	 */
	public abstract BungeePacketType getPacketType();

}
