package fr.badblock.api.bukkit.tech.receivers;

import com.mongodb.BasicDBObject;

import fr.badblock.api.bukkit.server.ServerManager;
import fr.badblock.api.bukkit.tech.TechManager;
import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.api.common.utils.GsonUtils;

public class PlayerDataReceiver extends RabbitListener 
{

	public PlayerDataReceiver(RabbitService rabbitService, String name, RabbitListenerType type, boolean debug)
	{
		super(TechManager.getRabbitService(), BadBungeeQueues.BUNGEE_DATA_PLAYERS + ServerManager.getServerName(), RabbitListenerType.SUBSCRIBER, true);
		load();
	}

	@Override
	public void onPacketReceiving(String body)
	{
		BasicDBObject databaseObject = GsonUtils.getGson().fromJson(body, BasicDBObject.class);
		
		
	}
	
}