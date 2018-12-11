package fr.badblock.api.common.minecraft;

import fr.badblock.api.common.sync.bungee.BadBungeeQueues;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.utils.GsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BungeeRequest
{

	private String		type;
	private String		content;
	
	private String toJson()
	{
		return GsonUtils.getGson().toJson(this);
	}
	
	public void send(RabbitService rabbitService)
	{
		RabbitPacketMessage packetMessage = new RabbitPacketMessage(30000, toJson());
		RabbitPacket packet = new RabbitPacket(packetMessage, BadBungeeQueues.BUNGEE_PROCESSING, 
				false, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER);
		
		rabbitService.sendPacket(packet);
	}
	
}
