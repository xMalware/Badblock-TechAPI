package fr.badblock.api.common.tech.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.google.gson.Gson;

import fr.badblock.api.common.tech.AutoReconnector;
import fr.badblock.api.common.tech.redis.methods.RedisMethod;
import fr.badblock.api.common.tech.redis.setting.RedisSettings;
import fr.badblock.api.common.tech.redis.threading.RedisThread;
import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class RedisService extends AutoReconnector
{

	private		String						name;
	private		RedisSettings				settings;
	private		Jedis						jedis;
	private		boolean						dead;
	private		Random						random;
	private		Queue<RedisMethod>			queue;
	private		List<RedisThread>			threads;

	public RedisService(String name, RedisSettings settings) 
	{
		super(name, settings);
		setSettings(settings);
		setName(name);
		setRandom(new Random());
		setQueue(new ConcurrentLinkedDeque<>());
		setThreads(new ArrayList<>());
		reconnect();
		for (int i=0;i<settings.getWorkerThreads();i++)
		{
			getThreads().add(new RedisThread(this, i));
		}
	}
	
	public void sendSyncPacket(RedisMethod redisMethod) throws Exception
	{
		redisMethod.work(this);
	}
	
	public void sendAsyncPacket(RedisMethod redisMethod)
	{
		getQueue().add(redisMethod);
		dislogeQueue();
	}

	private void dislogeQueue()
	{
		Optional<RedisThread> availableThread = getAvailableThread();
		if (isUnreachable(availableThread))
		{
			return;
		}
		RedisThread thread = availableThread.get();
		thread.stirHimself();
	}
	
	private boolean isUnreachable(Optional<?> optional)
	{
		return optional == null || !optional.isPresent();
	}
	
	private Optional<RedisThread> getAvailableThread()
	{
		return threads.stream().filter(thread -> thread.canHandlePacket()).findAny();
	}
	
	public void remove()
	{
		if (isDead())
		{
			Log.log(LogType.ERROR, "[RedisConnector] The service is already dead.");
			return;
		}
		long time = System.currentTimeMillis();
		setDead(true); // Set dead
		getTask().cancel(); // Cancel AutoReconnector task
		// Close channel
		/*try 
		{
			getJedis().close();
		}
		catch (Exception error)
		{
			Log.log(LogType.ERROR, "[RedisConnector] Something gone wrong while trying to close Redis connection.");
			error.printStackTrace();
			return;
		}*/
		RedisConnector.getInstance().getServices().remove(getName());
		Log.log(LogType.SUCCESS, "[RedisConnector] Redis service disconnected (" + (System.currentTimeMillis() - time) + " ms).");
	}

	public Gson getGson(boolean indented) 
	{
		RedisConnector redisConnector = RedisConnector.getInstance();
		if (indented)
		{
			return redisConnector.getExposedGson();
		}
		return redisConnector.getGson();
	}

	public boolean isAlive()
	{
		return !isDead();
	}

	@Override
	public boolean isConnected() 
	{
		return getJedis() != null && getJedis().isConnected();
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
			setJedis(getSettings().toFactory());
			Log.log(LogType.SUCCESS, "[RedisConnector] Successfully (re)connected to Redis service (" + (System.currentTimeMillis() - time) + " ms).");
		}
		catch(Exception error) 
		{
			error.printStackTrace();
			Log.log(LogType.ERROR, "[RedisConnector] Unable to connect to Redis service (" + error.getMessage() + ").");
		}
	}

}
