
package fr.badblock.api.common.sync.bungee.packets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
/**
 * Bungee packet
 * 
 * @author xMalware
 *
 */
public class BungeePacket {

	/**
	 * Bungee packet type
	 * 
	 * @param Set
	 *            the new Bungee packet type
	 * @return Returns the current Bungee packet type
	 */
	private BungeePacketType type;
	/**
	 * Bungee packet content
	 * 
	 * @param Set
	 *            the new Bungee packet contnet
	 * @return Returns the current Bungee packet content
	 */
	private String content;

}
