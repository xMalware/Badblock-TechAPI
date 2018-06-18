package fr.badblock.api.common.sync.bungee.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ServerBroadcastObject
{

	private	String	serverName;
	private	String	broadcastMessage;
	
}