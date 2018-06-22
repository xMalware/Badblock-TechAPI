package fr.badblock.api.bukkit.players;

import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

import com.mongodb.BasicDBObject;

import fr.badblock.api.bukkit.tech.receivers.PlayerDataReceiver;
import net.minecraft.server.v1_12_R1.EntityPlayer;

public class BadblockPlayer extends CraftPlayer
{

	private BasicDBObject databaseObject;
	
	public BadblockPlayer(CraftServer server, EntityPlayer entity)
	{
		super(server, entity);
		
		new Thread("loader-" + getName())
		{
			@Override
			public void run()
			{
				while (true)
				{
					Optional<Entry<String, BasicDBObject>> optional = PlayerDataReceiver.objectsToSet.entrySet().parallelStream().filter(entry ->
					{
						return entry.getValue().getString("name").equalsIgnoreCase(getName()) ||  
								(entry.getValue().containsField("nickname") && entry.getValue().getString("nickname").equalsIgnoreCase(getName()));
					}).findFirst();
					
					if (optional.isPresent())
					{
						Entry<String, BasicDBObject> entry = optional.get();
						databaseObject = entry.getValue();
						PlayerDataReceiver.objectsToSet.remove(entry.getKey());
						System.out.println("set ok");
					}
				}
			}
		}.start();
	}
	
}
