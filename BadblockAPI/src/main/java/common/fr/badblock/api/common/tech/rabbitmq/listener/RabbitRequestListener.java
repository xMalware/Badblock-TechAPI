package fr.badblock.api.common.tech.rabbitmq.listener;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public abstract class RabbitRequestListener
{

	private final RabbitService			rabbitService;
	private final String				name;
	private final boolean				debug;
	@Setter private Consumer			consumer;

	public void load()
	{
		try
		{
			if (!getRabbitService().isAlive()) 
			{
				return;
			}

			Channel channel = getRabbitService().getChannel();

			channel.queueDeclare(name, false, false, false, null);

			channel.basicQos(1);

			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					AMQP.BasicProperties replyProps = new AMQP.BasicProperties
							.Builder()
							.correlationId(properties.getCorrelationId())
							.build();

					String response = new String(body,"UTF-8");

					if (getRabbitService().isDead()) 
					{
						return;
					}
					try
					{
						RabbitPacketMessage rabbitMessage = RabbitPacketMessage.fromJson(response);
						if (rabbitMessage.isAlive()) 
						{
							if (isDebug()) 
							{
								Log.log(LogType.DEBUG, "[RabbitConnector] Received packet from " + getName() + ": " + rabbitMessage.getMessage());
							}
							
							response = reply(rabbitMessage.getMessage());

							channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
							channel.basicAck(envelope.getDeliveryTag(), false);
							
							// RabbitMq consumer worker thread notifies the RPC server owner thread 
							synchronized(this) {
								this.notify();
							}
						}
						else if (isDebug())
						{
							Log.log(LogType.ERROR, "[RabbitConnector] Error during a received packet from " + getName() + ": EXPIRED!");
							Log.log(LogType.ERROR, "[RabbitConnector] " + rabbitMessage.getMessage());
						}
					}
					catch(Exception error)
					{
						Log.log(LogType.ERROR, "[RabbitConnector] Error during the handle delivery.");
						error.printStackTrace();
					}
					

				}
			};

			channel.basicConsume(name, false, consumer);

			Log.log(LogType.SUCCESS, "[RabbitConnector] Loaded request listener from " + getName() + " (" + getClass().getSimpleName() + ").");
			// Wait and be prepared to consume the message from RPC client.
			while (true) {
				synchronized(consumer) {
					try {
						consumer.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();	    	
					}
				}
			}
		} catch (IOException e) {
			Log.log(LogType.ERROR, "[RabbitConnector] Error during a request listener bind.");
			e.printStackTrace();
		}
	}

	public abstract String reply(String body);

}
