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
	/**
	 * Logging packet
	 */
	LOG();

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
