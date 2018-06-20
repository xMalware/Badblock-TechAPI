package fr.badblock.api.common.tech.rabbitmq.packet;

import fr.badblock.api.common.utils.data.Callback;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RabbitPacket
{

	private RabbitPacketMessage		rabbitPacketMessage;
	private String					queue;
	private boolean 				debug;
	private RabbitPacketEncoder		encoder;
	private RabbitPacketType		type;
	private Callback<String>		callback;
	
	public RabbitPacket(RabbitPacketMessage rabbitPacketMessage, String queue, boolean debug, RabbitPacketEncoder rabbitPacketEncoder, RabbitPacketType rabbitPacketType)
	{
		this(rabbitPacketMessage, queue, debug, rabbitPacketEncoder, rabbitPacketType, null);
	}
	
}
