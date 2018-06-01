package fr.badblock.api.common.tech.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.badblock.api.common.tech.AutoReconnector;
import fr.badblock.api.common.tech.TechThread;
import fr.badblock.api.common.tech.sql.methods.SQLMethod;
import fr.badblock.api.common.tech.sql.packet.SQLPacket;
import fr.badblock.api.common.tech.sql.setting.SQLSettings;
import fr.badblock.api.common.tech.sql.thread.SQLMethodThread;
import fr.badblock.api.common.tech.sql.thread.SQLPacketThread;
import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import lombok.Getter;
import lombok.Setter;

public class SQLService extends AutoReconnector
{

	@Getter@Setter	private		String						name;
	@Getter@Setter	private		HikariDataSource			hikariDataSource;
	@Getter@Setter	private		HikariConfig				hikariConfig;
	@Getter@Setter	private		SQLSettings					settings;
					private		Statement					statement;
	@Getter@Setter	private		boolean						dead;
	@Getter@Setter	private		Random						random;
	@Getter@Setter	private		Queue<SQLPacket>			packetQueue;
	@Getter@Setter	private		Queue<SQLMethod>			methodQueue;
	@Getter@Setter	private		List<TechThread<?>>			methodThreads;
	@Getter@Setter	private		List<TechThread<?>>			packetThreads;

	public SQLService(String name, SQLSettings settings) 
	{
		super(name, settings);
		setSettings(settings);
		setName(name);
		setRandom(new Random());
		setPacketQueue(new ConcurrentLinkedDeque<>());
		setMethodQueue(new ConcurrentLinkedDeque<>());
		setPacketThreads(new ArrayList<>());
		setMethodThreads(new ArrayList<>());
		setHikariConfig(getSettings().toFactory());
		reconnect();
		for(int i=0;i<settings.getWorkerThreads();i++)
		{
			getMethodThreads().add(new SQLMethodThread(this, i));
		}
		for(int i=0;i<settings.getWorkerThreads();i++)
		{
			getPacketThreads().add(new SQLPacketThread(this, i));
		}
	}

	public void sendSyncPacket(SQLPacket sqlPacket)
	{
		handle(sqlPacket);
	}

	public void sendAsyncPacket(SQLPacket sqlPacket)
	{
		getPacketQueue().add(sqlPacket);
		dislogeQueue(getPacketThreads(), getPacketQueue());
	}

	public void sendAsyncPacket(SQLMethod sqlMethod)
	{
		getMethodQueue().add(sqlMethod);
		dislogeQueue(getMethodThreads(), getMethodQueue());
	}
	
	private <T> void dislogeQueue(List<TechThread<?>> threads, Queue<?> queue)
	{
		Optional<TechThread<?>> availableThread = getAvailableThread(threads);
		if (isUnreachable(availableThread))
		{
			return;
		}
		TechThread<?> thread = availableThread.get();
		thread.stirHimself();
	}
	
	private boolean isUnreachable(Optional<?> optional)
	{
		return optional == null || !optional.isPresent();
	}
	
	private <T> Optional<TechThread<?>> getAvailableThread(List<TechThread<?>> threads)
	{
		return threads.stream().filter(thread -> thread.canHandlePacket()).findAny();
	}

	public void handle(SQLPacket sqlPacket)
	{
		switch (sqlPacket.getType())
		{
		case GETTER:
			try 
			{
				Statement statement = getStatement();
				ResultSet resultSet = statement.executeQuery(sqlPacket.getQuery());
				sqlPacket.done(resultSet);
				if (sqlPacket.canCloseResultSet())
				{
					resultSet.close();
				}
			}
			catch (Exception error)
			{
				error.printStackTrace();
			}
			break;
		case SETTER:
			try 
			{
				Statement statement = getStatement();
				statement.executeUpdate(sqlPacket.getQuery());
			}
			catch (Exception error)
			{
				error.printStackTrace();
			}
			break;
		default:
			Log.log(LogType.ERROR, "[SQLConnector] Unknown packet type to handle (" + sqlPacket.getType() + ").");
		}
	}

	public void sendSyncPacket(SQLMethod sqlMethod)
	{
		sqlMethod.run(this);
	}

	private Statement getStatement() throws SQLException
	{
		if (statement == null)
		{
			statement = getConnection().createStatement();
		}
		return statement;
	}

	public Connection getConnection()
	{
		try
		{
			return getHikariDataSource().getConnection();
		}
		catch (SQLException exception)
		{
			Log.log(LogType.ERROR, "[SQLConnector] Couldn't get connection.");
			exception.printStackTrace();
			return null;
		}
	}

	public void remove()
	{
		if (isDead())
		{
			Log.log(LogType.ERROR, "[SQLConnector] The service is already dead.");
			return;
		}
		long time = System.currentTimeMillis();
		setDead(true); // Set dead
		getTask().cancel(); // Cancel AutoReconnector task
		// Close channel
		try 
		{
			getStatement().close();
			getConnection().close();
			getHikariDataSource().close();
		}
		catch (Exception error)
		{
			Log.log(LogType.ERROR, "[SQLConnector] Something gone wrong while trying to close SQL connection.");
			error.printStackTrace();
			return;
		}
		SQLConnector.getInstance().getServices().remove(getName());
		Log.log(LogType.SUCCESS, "[SQLConnector] SQL service disconnected (" + (System.currentTimeMillis() - time) + " ms).");
	}

	public Gson getGson(boolean indented) 
	{
		SQLConnector sqlConnector = SQLConnector.getInstance();
		if (indented)
		{
			return sqlConnector.getExposeGson();
		}
		return sqlConnector.getGson();
	}

	public boolean isAlive()
	{
		return !isDead();
	}

	@Override
	public boolean isConnected() 
	{
		return getHikariConfig() != null && getHikariDataSource() != null && !getHikariDataSource().isClosed();
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
			setHikariDataSource(new HikariDataSource(getHikariConfig()));
			Log.log(LogType.SUCCESS, "[SQLConnector] Successfully (re)connected to SQL service (" + (System.currentTimeMillis() - time) + " ms).");
		}
		catch(Exception error) 
		{
			error.printStackTrace();
			setHikariConfig(getSettings().toFactory());
			Log.log(LogType.ERROR, "[SQLConnector] Unable to connect to SQL service (" + error.getMessage() + ").");
		}
	}

}
