package fr.badblock.api.common.tech.rabbitmq.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RabbitPacketEncoder
{

	UTF8("UTF-8");
	
	private String name;
	
}
