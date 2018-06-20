package fr.badblock.api.common.sync.bungee;

import fr.badblock.api.common.sync.bungee.objects.ServerObject;
import fr.badblock.api.common.sync.bungee.packets.BungeePacket;
import fr.badblock.api.common.sync.bungee.packets.BungeePacketType;
import fr.badblock.api.common.sync.bungee.states.BungeeStatePacket;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.utils.GsonUtils;

/**
 * 
 * BungeeUtils
 * 
 * @author xMalware
 *
 */
public class BungeeUtils
{

	/**
	 * Add a server
	 * @param rabbitService
	 * @param serverObject
	 */
	public static void addServer(RabbitService rabbitService, ServerObject serverObject)
	{
		// To json
		String message = GsonUtils.getGson().toJson(serverObject);
		// Send a bungee packet
		sendBungeePacket(rabbitService, new BungeePacket(BungeePacketType.ADD_SERVER, message));
	}

	/**
	 * Remove a server
	 * @param rabbitService
	 * @param server name
	 */
	public static void removeServer(RabbitService rabbitService, String serverName)
	{
		// Send a bungee packet
		sendBungeePacket(rabbitService, new BungeePacket(BungeePacketType.REMOVE_SERVER, serverName));
	}

	/**
	 * Send a bungee packet
	 * @param rabbitService
	 * @param bungeePacket
	 */
	public static void sendBungeePacket(RabbitService rabbitService, BungeePacket bungeePacket)
	{
		// To json
		String json = GsonUtils.getGson().toJson(bungeePacket);
		// Create a rabbit packet message
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(5000, json);
		// Create a rabbit packet
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, BadBungeeQueues.BUNGEE_PROCESSING, false, 
				RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER);
		// Send the rabbit packet
		rabbitService.sendPacket(rabbitPacket);
	}

	/**
	 * Send a bungee state
	 * @param rabbitService
	 * @param packet
	 */
	public static void sendBungeeState(RabbitService rabbitService, BungeeStatePacket packet)
	{
		// To json
		String json = GsonUtils.getGson().toJson(packet);
		// Create a rabbit packet message
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(5000, json);
		// Create a rabbit packet
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, BadBungeeQueues.BUNGEE_STATE, false, 
				RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER);
		// Send the rabbit packet
		rabbitService.sendPacket(rabbitPacket);
	}

}
