package fr.badblock.api.bukkit.players;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import net.minecraft.server.v1_12_R1.EntityPlayer;

public class BadblockPlayer extends CraftPlayer
{

	public BadblockPlayer(CraftServer server, EntityPlayer entity)
	{
		super(server, entity);
	}
	
}
