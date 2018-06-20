package fr.badblock.api.common.tech.rabbitmq.listener;

import java.io.IOException;
import java.util.UUID;

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
		new Thread("RabbitMQ/RequestListener/" + name + "/" + UUID.randomUUID().toString())
		{
			@Override
			public void start()
			{
				Channel channel = null;
				try
				{
					if (!getRabbitService().isAlive()) 
					{
						return;
					}
					System.out.println("Request listener : A");

					System.out.println("Request listener : B");
					channel = getRabbitService().getConnection().createChannel();

					final Channel finalChannel = channel;

					channel.queueDeclare(name, false, false, false, null);

					channel.basicQos(1);

					System.out.println("Request listener : C");
					Consumer consumer = new DefaultConsumer(channel) {
						@Override
						public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
							System.out.println("Request listener : D - HANDLE DELIVERY");
							AMQP.BasicProperties replyProps = new AMQP.BasicProperties
									.Builder()
									.correlationId(properties.getCorrelationId())
									.build();

							String response = new String(body,"UTF-8");

							if (getRabbitService().isDead()) 
							{
								return;
							}
							System.out.println("Request listener : E - HANDLE DELIVERY");
							try
							{
								System.out.println("Request listener : F - HANDLE DELIVERY");
								RabbitPacketMessage rabbitMessage = RabbitPacketMessage.fromJson(response);
								if (rabbitMessage.isAlive()) 
								{
									System.out.println("Request listener : G - HANDLE DELIVERY");
									if (isDebug()) 
									{
										System.out.println("Request listener : H - HANDLE DELIVERY");
										Log.log(LogType.DEBUG, "[RabbitConnector] Received packet from " + getName() + ": " + rabbitMessage.getMessage());
									}

									System.out.println("Request listener : I - HANDLE DELIVERY");
									response = reply(rabbitMessage.getMessage());

									finalChannel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
									finalChannel.basicAck(envelope.getDeliveryTag(), false);
								}
								else if (isDebug())
								{
									System.out.println("Request listener : J - HANDLE DELIVERY");
									Log.log(LogType.ERROR, "[RabbitConnector] Error during a received packet from " + getName() + ": EXPIRED!");
									Log.log(LogType.ERROR, "[RabbitConnector] " + rabbitMessage.getMessage());
								}
							}
							catch(Exception error)
							{
								Log.log(LogType.ERROR, "[RabbitConnector] Error during the handle delivery.");
								error.printStackTrace();
							}
							finally {
								System.out.println("Request listener : J2 - HANDLE DELIVERY");
								finalChannel.basicPublish( "", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
								finalChannel.basicAck(envelope.getDeliveryTag(), false);
								// RabbitMq consumer worker thread notifies the RPC server owner thread 
								synchronized(this) {
									this.notify();
								}
							}
							System.out.println("Request listener : K - HANDLE DELIVERY");


						}
					};
					System.out.println("Request listener : L - HANDLE DELIVERY");

					channel.basicConsume(name, true, consumer);
					System.out.println("Request listener : M - HANDLE DELIVERY");
					while (true) {
						System.out.println("Request listener : N - HANDLE DELIVERY");
						synchronized(consumer) {
							try {
								consumer.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();	    	
							}
						}
					}
				} catch (Exception e) {
					Log.log(LogType.ERROR, "[RabbitConnector] Error during a request listener bind.");
					e.printStackTrace();
				}
				finally {
					if (channel != null)
						try {
							channel.close();
						} catch (Exception _ignore) {}
				}
			}
		}.start();
	}

	public abstract String reply(String body);

}
