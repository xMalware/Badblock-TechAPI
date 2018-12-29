package fr.badblock.api.common.sync.bungee.objects;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ServerObject
{

	private	String	name;
	private	String	ip;
	private int			port;
	
}
