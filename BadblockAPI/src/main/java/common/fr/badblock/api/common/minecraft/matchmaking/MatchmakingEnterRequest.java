package fr.badblock.api.common.minecraft.matchmaking;

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
public class MatchmakingEnterRequest
{

	private String		playerName;
	private String			cluster;
	private String		worldSystem;
	
	private String toJson()
	{
		return GsonUtils.getGson().toJson(this);
	}
	
	public void send(RabbitService rabbitService)
	{
		RabbitPacketMessage packetMessage = new RabbitPacketMessage(30000, toJson());
		RabbitPacket packet = new RabbitPacket(packetMessage, MatchmakingQueues.ENTER_REQUEST + "." + cluster, 
				false, RabbitPacketEncoder.UTF8, RabbitPacketType.MESSAGE_BROKER);
		
		rabbitService.sendPacket(packet);
	}
	
}
