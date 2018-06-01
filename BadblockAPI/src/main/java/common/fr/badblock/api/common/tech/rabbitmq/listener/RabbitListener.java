package fr.badblock.api.common.tech.rabbitmq.listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;

import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public abstract class RabbitListener
{

	private final RabbitService			rabbitService;
	private final String				name;
	private final RabbitListenerType	type;
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
			String finalQueueName = getName();
			switch (getType()) 
			{
			case MESSAGE_BROKER:
				channel.queueDeclare(getName(), false, false, false, null);
				break;
			case SUBSCRIBER:
				channel.exchangeDeclare(getName(), "fanout");
				finalQueueName = channel.queueDeclare().getQueue();
				channel.queueBind(finalQueueName, getName(), "");
				break;
			default:
				Log.log(LogType.ERROR, "Unknown listener type.");
			}
			setConsumer(new RabbitListenerConsumer(channel, this));
			channel.basicConsume(finalQueueName, true, getConsumer());
			Log.log(LogType.SUCCESS, "[RabbitConnector] Loaded listener from " + getName() + " (" + getClass().getSimpleName() + ").");
		}
		catch(Exception error) 
		{
			Log.log(LogType.ERROR, "[RabbitConnector] Error during a listener bind.");
			error.printStackTrace();
		}
	}

	public abstract void onPacketReceiving(String body);

}
