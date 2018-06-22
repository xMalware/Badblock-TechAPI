package fr.badblock.api.bukkit.server;

import org.bukkit.Bukkit;

import lombok.Getter;

public class ServerManager
{

	@Getter
	private static ServerManager	instance = new ServerManager();
	
	public static String getServerName()
	{
		return Bukkit.getServerName();
	}
	
}
