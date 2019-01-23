package fr.badblock.api.common.sync.bungee.packets;

import fr.badblock.api.common.sync.bungee._BungeeProcessing;
import lombok.Getter;

@Getter
/**
 * 
 * Bungee packet types
 * 
 * @author xMalware
 *
 */
public enum BungeePacketType {

	/**
	 * Broadcast packet
	 */
	BROADCAST(),
	
	/** Broadcast components packet **/
	BROADCAST_COMPONENT(),
	
	/**
	 * Logging packet
	 */
	LOG(),
	
	/**
	 * Add server
	 */
	ADD_SERVER(),
	
	/**
	 * Remove server
	 */
	REMOVE_SERVER(),
	
	/**
	 * Force kick
	 */
	FORCE_KICK(),

	/**
	 * Server broadcast
	 */
	SERVER_BROADCAST(),
	
	/**
	 * Permission broadcast
	 */
	PERMISSION_BROADCAST();

	/**
	 * Custom packet processing
	 * 
	 * @return The custom packet processing
	 */
	private _BungeeProcessing process;

	/**
	 * Set the process
	 * @param bungeeProcessing
	 */
	public void setProcess(_BungeeProcessing bungeeProcessing)
	{
		// Set process
		this.process = bungeeProcessing;
	}

}
