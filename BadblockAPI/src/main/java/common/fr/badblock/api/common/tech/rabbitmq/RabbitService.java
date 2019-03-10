package fr.badblock.api.common.tech.rabbitmq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import fr.badblock.api.common.tech.AutoReconnector;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitRequestListener;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketManager;
import fr.badblock.api.common.tech.rabbitmq.setting.RabbitSettings;
import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RabbitService extends AutoReconnector
{
	
	private ConnectionFactory		connectionFactory;
	private Connection				connection;
	private	Channel					channel;
	private RabbitSettings			settings;
	private boolean					dead;

	private List<RabbitListener>		listeners = new ArrayList<>();
	private List<RabbitRequestListener>	requests = new ArrayList<>();

	public RabbitService(String name, RabbitSettings settings)
	{
		super(name, settings);
		setSettings(settings);
		reconnect();
	}

	public RabbitService addListener(RabbitListener listener)
	{
		listener.load();
		listeners.add(listener);
		return this;
	}

	public RabbitService addRequestListener(RabbitRequestListener listener)
	{
		listener.load();
		requests.add(listener);
		return this;
	}

	private void debugPacket(RabbitPacket rabbitPacket)
	{
		if (!rabbitPacket.isDebug())
		{
			return;	
		}
		Log.log(LogType.DEBUG, "[RabbitConnector] Packet sended to '" + rabbitPacket.getQueue() + "' : " + rabbitPacket.getRabbitPacketMessage().getMessage());
	}
	
	public void sendSyncPacket(RabbitPacket rabbitPacket) throws Exception
	{
		Channel channel = getChannel();
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

			System.out.println("O");
			channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					System.out.println("Response: " + properties.getCorrelationId() + " / " + corrId);
					if (properties.getCorrelationId().equals(corrId)) {
						response.offer(new String(body, "UTF-8"));
					}
				}
			});
			System.out.println("K!");
			rabbitPacket.getCallback().done(response.take(), null);
			System.out.println("!! :P");

			debugPacket(rabbitPacket);
			break;
		}
	}
	
	public void sendPacket(RabbitPacket rabbitPacket)
	{
		getPacketManager().sendPacket(rabbitPacket);
	}
	
	@Override
	public void remove()
	{
		if (isDead())
		{
			Log.log(LogType.ERROR, "[RabbitConnector] The service is already dead.");
			return;
		}
		long time = System.currentTimeMillis();
		setDead(true); // Set dead
		getTask().cancel(); // Cancel AutoReconnector task
		// Close channel
		try
		{
			getChannel().close();
		}
		catch (Exception error)
		{
			Log.log(LogType.ERROR, "[RabbitConnector] Something gone wrong while trying to close RabbitMQ channel.");
			Log.log(LogType.ERROR, "[RabbitConnector] Otherwhise, we are trying to close connection..");
			error.printStackTrace();
		}
		// Close connection
		try
		{
			getConnection().close();
		}
		catch (Exception error)
		{
			Log.log(LogType.ERROR, "[RabbitConnector] Something gone wrong while trying to close RabbitMQ connection.");
			error.printStackTrace();
			return;
		}
		RabbitConnector.getInstance().getServices().remove(this.getName());
		Log.log(LogType.SUCCESS, "[RabbitConnector] RabbitMQ service disconnected (" + (System.currentTimeMillis() - time) + " ms).");
	}

	public boolean isAlive()
	{
		return !isDead();
	}

	@Override
	public boolean isConnected() 
	{
		return getConnection() != null && getConnection().isOpen() &&
				getChannel() != null && getChannel().isOpen();
	}

	@Override
	public void reconnect() 
	{
		if (isDead())
		{
			return;
		}
		if (isConnected()) 
		{
			return;
		}
		try 
		{
			long time = System.currentTimeMillis();
			setConnectionFactory(getSettings().toFactory());
			// Create connection
			if (getConnection() == null || !getConnection().isOpen())
				setConnection(getConnectionFactory().newConnection());
			// Create channel
			if (getChannel() == null || !getChannel().isOpen())
				setChannel(getConnection().createChannel());
			// Reload listeners
			listeners.stream().forEach(listener -> listener.load());
			// Reload request listeners
			requests.stream().forEach(request -> request.load());
			Log.log(LogType.SUCCESS, "[RabbitConnector] Successfully reconnected to RabbitMQ service (" + (System.currentTimeMillis() - time) + " ms).");
		}
		catch(Exception error) 
		{
			error.printStackTrace();
			setConnectionFactory(getSettings().toFactory());
			Log.log(LogType.ERROR, "Unable to connect to RabbitMQ service (" + error.getMessage() + ")");
		}
	}

	public RabbitPacketManager getPacketManager()
	{
		return RabbitPacketManager.getInstance(this);
	}

}
