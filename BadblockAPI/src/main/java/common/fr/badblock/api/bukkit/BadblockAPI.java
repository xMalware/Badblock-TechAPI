package fr.badblock.api.bukkit;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.badblock.api.bukkit.tech.TechManager;
import fr.badblock.api.bukkit.utils.java.PackageUtils;
import lombok.Getter;

@Getter
public class BadblockAPI extends JavaPlugin
{
	
	@Getter
	private static BadblockAPI	instance; 
	
	private TechManager	techManager;
	
	@Override
	public void onLoad()
	{
		instance = this;
		try
		{
			techManager = new TechManager(this);
		}
		catch (Exception exception)
		{
			Bukkit.getConsoleSender().sendMessage("§c[BadblockAPI] An error occurred while loading the API.");
			exception.printStackTrace();
			Bukkit.shutdown();
		}
	}
	
	@Override
	public void onEnable()
	{
		try
		{
			PackageUtils.instanciateListeners(this,
					"fr.badblock.api.bukkit.listeners.players",
					"fr.badblock.api.bukkit.tech.receivers"
			);
		}
		catch (IOException exception)
		{
			Bukkit.getConsoleSender().sendMessage("§c[BadblockAPI] An error occurred while loading the API.");
			exception.printStackTrace();
			Bukkit.shutdown();
		}
	}
	
}
