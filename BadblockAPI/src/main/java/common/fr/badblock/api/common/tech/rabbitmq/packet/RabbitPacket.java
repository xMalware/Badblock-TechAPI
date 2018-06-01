package fr.badblock.api.common.tech.rabbitmq.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RabbitPacket
{

	private RabbitPacketMessage		rabbitMessage;
	private String					queue;
	private boolean 				debug;
	private RabbitPacketEncoder		encoder;
	private RabbitPacketType		type;
	
}
