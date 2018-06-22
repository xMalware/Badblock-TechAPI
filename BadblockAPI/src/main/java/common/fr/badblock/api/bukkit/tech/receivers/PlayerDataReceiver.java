package fr.badblock.api.bukkit.tech.receivers;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;

import fr.badblock.api.bukkit.server.ServerManager;
import fr.badblock.api.bukkit.tech.TechManager;
import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.api.common.utils.GsonUtils;

public class PlayerDataReceiver extends RabbitListener 
{

	public static Map<String, BasicDBObject> objectsToSet = new HashMap<>();
	
	public PlayerDataReceiver()
	{
		super(TechManager.getRabbitService(), BadBungeeQueues.BUNGEE_DATA_PLAYERS + ServerManager.getServerName(), RabbitListenerType.SUBSCRIBER, true);
		System.out.println(BadBungeeQueues.BUNGEE_DATA_PLAYERS + ServerManager.getServerName());
		load();
	}

	@Override
	public void onPacketReceiving(String body)
	{
		BasicDBObject databaseObject = GsonUtils.getGson().fromJson(body, BasicDBObject.class);
		
		if (!databaseObject.containsField("name"))
		{
			System.out.println("err");
		}
		
		String name = databaseObject.getString("name");
		System.out.println("add");
		objectsToSet.put(name, databaseObject);
	}
	
}