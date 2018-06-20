package fr.badblock.api.common.sync.bungee.states;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 
 * Bungee state packet
 * 
 * @author xMalware
 *
 */
@AllArgsConstructor
@Data
public class BungeeStatePacket
{

	private String		name;
	private BungeeState	state;
	
}
