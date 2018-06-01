package fr.badblock.api.common.tech.rabbitmq.packet;

import fr.badblock.api.common.tech.rabbitmq.RabbitConnector;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class RabbitPacketMessage 
{

	private long   expire = -1L;
	private String message;

	public RabbitPacketMessage(long ttl, String message) 
	{
		if (ttl > 0) setExpire(System.currentTimeMillis() + ttl);
		setMessage(message);
	}
	
	public RabbitPacketMessage(String message) 
	{
		setMessage(message);
	}
	
	public boolean isAlive() 
	{
		return !isExpired();
	}
	
	public boolean isExpired() 
	{
		return getExpire() != -1L && getExpire() < System.currentTimeMillis();
	}
	
	public String toJson() 
	{
		return RabbitConnector.getInstance().getGson().toJson(this);
	}
	
	public static RabbitPacketMessage fromJson(String message)
	{
		return RabbitConnector.getInstance().getGson().fromJson(message, RabbitPacketMessage.class);
	}
	
}
