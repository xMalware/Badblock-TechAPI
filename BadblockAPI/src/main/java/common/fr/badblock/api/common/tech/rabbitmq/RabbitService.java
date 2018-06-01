package fr.badblock.api.common.tech.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import fr.badblock.api.common.tech.AutoReconnector;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketManager;
import fr.badblock.api.common.tech.rabbitmq.setting.RabbitSettings;
import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class RabbitService extends AutoReconnector
{
	
	private ConnectionFactory		connectionFactory;
	private Connection				connection;
	private	Channel					channel;
	private RabbitSettings			settings;
	private boolean					dead;

	private List<RabbitListener>	listeners = new ArrayList<>();

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
