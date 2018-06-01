package fr.badblock.api.common.tech.rabbitmq.packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.threading.RabbitThread;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class RabbitPacketManager 
{

	private static Map<RabbitService, RabbitPacketManager>	instances		= new ConcurrentHashMap<>();
	
	private List<RabbitThread>								threads			= new ArrayList<>();
	private	Queue<RabbitPacket>								queue			= new ConcurrentLinkedDeque<>();
	private RabbitService									rabbitService;

	
	RabbitPacketManager(RabbitService rabbitService)
	{
		setRabbitService(rabbitService);
		for(int i=0;i<rabbitService.getSettings().getWorkerThreads();i++)
		{
			getThreads().add(new RabbitThread(this, i));
		}
	}
	
	public void sendPacket(RabbitPacket rabbitPacket)
	{
		getQueue().add(rabbitPacket);
		dislogeQueue();
	}
	
	private void dislogeQueue()
	{
		Optional<RabbitThread> availableThread = getAvailableThread();
		if (isUnreachable(availableThread))
		{
			return;
		}
		RabbitThread thread = availableThread.get();
		thread.stirHimself();
	}
	
	private boolean isUnreachable(Optional<?> optional)
	{
		return optional == null || !optional.isPresent();
	}
	
	private Optional<RabbitThread> getAvailableThread()
	{
		return threads.stream().filter(thread -> thread.canHandlePacket()).findAny();
	}

	public boolean isAlive() 
	{
		return !getRabbitService().isDead();
	}

	public static RabbitPacketManager getInstance(RabbitService rabbitService) 
	{
		RabbitPacketManager packetManager = instances.get(rabbitService);
		if (packetManager == null) 
		{
			packetManager = new RabbitPacketManager(rabbitService);
			instances.put(rabbitService, packetManager);
		}
		return packetManager;
	}
	
}
