package fr.badblock.api.common.tech.rabbitmq.threading;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import fr.badblock.api.common.tech.TechThread;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketManager;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
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
		Channel channel = rabbitService.getChannel();
		if (rabbitPacket == null)
		{
			return;
		}
		if (rabbitPacket.getRabbitPacketMessage() == null)
		{
			return;
		}
		String message = rabbitPacket.getRabbitPacketMessage().toJson();
		switch (rabbitPacket.getType())
		{
		case MESSAGE_BROKER:
			channel.queueDeclare(rabbitPacket.getQueue(), false, false, false, null);
			channel.basicPublish("", rabbitPacket.getQueue(), null, message.getBytes(rabbitPacket.getEncoder().getName()));
			debugPacket(rabbitPacket);
			break;
		case PUBLISHER:
			channel.exchangeDeclare(rabbitPacket.getQueue(), "fanout");
			channel.basicPublish(rabbitPacket.getQueue(), "", null, message.getBytes(rabbitPacket.getEncoder().getName()));
			debugPacket(rabbitPacket);
			break;
		case REMOTE_PROCEDURE_CALL:
			if (rabbitPacket.getCallback() == null)
			{
				break;
			}
			String replyQueueName = channel.queueDeclare().getQueue();
			final String corrId = UUID.randomUUID().toString();
			AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
					.correlationId(corrId)
					.replyTo(replyQueueName)
					.build();
			channel.basicPublish("", rabbitPacket.getQueue(), properties, message.getBytes(rabbitPacket.getEncoder().getName()));

			final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

			channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					if (properties.getCorrelationId().equals(corrId)) {
						response.offer(new String(body, "UTF-8"));
						try {
							String message = response.take();
							rabbitPacket.getCallback().done(message, null);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			});

			debugPacket(rabbitPacket);
			break;
		}
		
		message = null;
		channel = null;
		rabbitService = null;
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

	private void debugPacket(RabbitPacket rabbitPacket)
	{
		if (!rabbitPacket.isDebug())
		{
			return;	
		}
		Log.log(LogType.DEBUG, "[RabbitConnector] Packet sended to '" + rabbitPacket.getQueue() + "' : " + rabbitPacket.getRabbitPacketMessage().getMessage());
	}

}
