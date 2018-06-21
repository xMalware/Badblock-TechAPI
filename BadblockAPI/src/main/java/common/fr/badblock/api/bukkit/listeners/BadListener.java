package fr.badblock.api.bukkit.listeners;

import org.bukkit.event.Listener;

import fr.badblock.api.bukkit.BadblockAPI;

public class BadListener implements Listener
{

	public BadListener()
	{
		BadblockAPI instance = BadblockAPI.getInstance();
		instance.getServer().getPluginManager().registerEvents(this, instance);
	}
	
}
