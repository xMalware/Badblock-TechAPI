package fr.badblock.api.common.tech.mongodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import fr.badblock.api.common.tech.AutoReconnector;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import fr.badblock.api.common.tech.mongodb.setting.MongoSettings;
import fr.badblock.api.common.tech.mongodb.threading.MongoThread;
import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class MongoService extends AutoReconnector
{

	private		String						name;
	private		MongoSettings				settings;
	private		MongoClient					mongoClient;
	private		boolean						isDead;
	private     DB							db;
	private		Random						random;
	private 	List<MongoThread>			threads;
	private		Queue<MongoMethod>			queue;

	public MongoService(String name, MongoSettings settings)
	{
		super(name, settings);
		this.setSettings(settings);
		this.setName(name);
		this.setRandom(new Random());
		this.setThreads(new ArrayList<>());
		this.setQueue(new ConcurrentLinkedDeque<>());
		// Connect
		this.reconnect();
		// Load threads
		for (int i=0;i<getSettings().getWorkerThreads();i++)
		{
			getThreads().add(new MongoThread(this, i));
		}
	}

	public void useAsyncMongo(MongoMethod mongoMethod)
	{
		getQueue().add(mongoMethod);
		dislogeQueue();
	}

	private void dislogeQueue()
	{
		Optional<MongoThread> availableThread = getAvailableThread();
		if (isUnreachable(availableThread))
		{
			return;
		}
		MongoThread thread = availableThread.get();
		thread.stirHimself();
	}
	
	private boolean isUnreachable(Optional<?> optional)
	{
		return optional == null || !optional.isPresent();
	}
	
	private Optional<MongoThread> getAvailableThread()
	{
		return threads.stream().filter(thread -> thread.canHandlePacket()).findAny();
	}

	public DB db()
	{
		return this.getDb();
	}

	public MongoClient client() 
	{
		return this.getMongoClient();
	}

	public boolean isAlive()
	{
		return !isDead();
	}
	
	public void remove() 
	{
		if (isDead())
		{
			Log.log(LogType.ERROR, "[MongoConnector] The service is already dead.");
			return;
		}
		long time = System.currentTimeMillis();
		setDead(true); // Set dead
		getTask().cancel(); // Cancel AutoReconnector task
		// Close channel
		try
		{
			db().getMongo().close();
		}
		catch (Exception error)
		{
			Log.log(LogType.ERROR, "[MongoConnector] Something gone wrong while trying to close Mongo.");
			error.printStackTrace();
			return;
		}
		MongoConnector.getInstance().getServices().remove(this.getName());
		Log.log(LogType.SUCCESS, "[MongoConnector] Mongo service disconnected (" + (System.currentTimeMillis() - time) + " ms).");
	}

	@Override
	public boolean isConnected() 
	{
		// Disgusting method, TODO find something better
		if (db() == null)
			return false;
		try 
		{
			// test
			getDb().collectionExists("test");
			return true;
		} 
		catch (Exception exception)
		{
			exception.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("deprecation")
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
			setMongoClient(getSettings().toFactory());
			setDb(getMongoClient().getDB(settings.getDatabase()));
			Log.log(LogType.SUCCESS, "[MongoConnector] Successfully (re)connected to MongoDB service (" + (System.currentTimeMillis() - time) + " ms).");
		}
		catch(Exception error) 
		{
			error.printStackTrace();
			Log.log(LogType.ERROR, "[MongoConnector] Unable to connect to MongoDB service (" + error.getMessage() + ").");
		}
	}

}
