package fr.badblock.api.common.tech.rabbitmq.threading;

import fr.badblock.api.common.tech.TechThread;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitThread extends TechThread<RabbitPacket> 
{

	private RabbitPacketManager packetManager;

	public RabbitThread(RabbitPacketManager packetManager, int id)
	{
		super("RabbitThread", packetManager.getQueue(), id);
		setPacketManager(packetManager);
	}

	@Override
	public void work(RabbitPacket rabbitPacket) throws Exception
	{
		RabbitService rabbitService = getPacketManager().getRabbitService();
		rabbitService.sendSyncPacket(rabbitPacket);
	}

	@Override
	public String getErrorMessage()
	{
		return "[RabbitConnector] An error occurred while trying to send packet.";
	}

	@Override
	public boolean isServiceAlive()
	{
		return getPacketManager().isAlive();
	}

}
