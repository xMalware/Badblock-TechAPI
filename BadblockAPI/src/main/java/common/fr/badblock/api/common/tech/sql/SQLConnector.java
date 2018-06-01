package fr.badblock.api.common.tech.sql;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.badblock.api.common.tech.sql.setting.SQLSettings;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Main class where all interactions with SQL technology should start by this, 
 * for creating different services, with credentials
 * @author xMalware
 */
@Data public class SQLConnector 
{

	// SQLConnector singleton instance
	@Getter@Setter private static 	SQLConnector 								instance		= new SQLConnector();

	// Private fields
	private							GsonBuilder									gsonBuilder		= new GsonBuilder();
	private							Gson										gson			= getGsonBuilder().create();
	private							Gson										exposeGson		= getGsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private 					   	ConcurrentMap<String, SQLService>			services		= new ConcurrentHashMap<>();

	/**
	 * Create settings and be back with a SQLSettings object who is useful for some operations, like using it in different services
	 * @param hostnames   > hostnames, we higly recommend DNS
	 * @param port 		  > SQL Cluster port, 3306 by default
	 * @param password	  > the password of that account
	 * @param database 	  > database id which will be used
	 * @return a SQLSettings object
	 */
	public SQLSettings createSettings(String[] hostnames, int port, String username, String database, String password, int maxPoolSize, long maxLifetime, long idleTimeout, long leakDetectionThreshold, long connectionTimeout, int workerThreads)
	{
		return new SQLSettings(hostnames, port, username, database, password, maxPoolSize, maxLifetime, idleTimeout, leakDetectionThreshold, connectionTimeout, workerThreads);
	}

	/**
	 * Adding a new service
	 * @param name 		  	   > name of the service
	 * @param sqlSettings      > credentials
	 * @return a SQLService object ready to work
	 */
	public SQLService createService(String name, SQLSettings sqlSettings)
	{
		return new SQLService(name, sqlSettings);
	}

	/**
	 * Register a new service
	 * @param redisService		> Redis service
	 * @return 
	 */
	public SQLService registerService(SQLService sqlService)
	{
		services.put(sqlService.getName(), sqlService);
		return sqlService;
	}

	/**
	 * Unregister an existing service
	 * @param redisService		> Redis service
	 */
	public SQLService unregisterService(SQLService sqlService) 
	{
		services.remove(sqlService.getName());
		return sqlService;
	}

}
