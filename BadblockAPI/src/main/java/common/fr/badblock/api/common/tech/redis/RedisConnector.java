package fr.badblock.api.common.tech.redis;

import fr.badblock.api.common.tech.Connector;
import fr.badblock.api.common.tech.redis.setting.RedisSettings;
import lombok.Getter;
import lombok.Setter;

/**
 * Main class where all interactions with Redis technology should start by this, 
 * for creating different services, with credentials and by the way getting different 
 * services to apply some useful things, like using the key/value storage
 * @author xMalware
 */
public class RedisConnector extends Connector<RedisService>
{

	// RedisConnector singleton instance
	@Getter@Setter private static 	RedisConnector 								instance		= new RedisConnector();
	
	/**
	 * Create settings and be back with a RedisSettings object who is useful for some operations, like using it in different services
	 * @param hostnames   > hostnames, we higly recommend DNS
	 * @param port 		  > Redis Cluster port, 6379 by default
	 * @param password	  > the password of that account
	 * @param database 	  > database id which will be used
	 * @return a RedisSettings object
	 */
	public RedisSettings createSettings(String[] hostnames, int port, String password, int database, int workerThreads)
	{
		return new RedisSettings(hostnames, port, password, database, workerThreads);
	}
	
	/**
	 * Adding a new service
	 * @param name 		  	   > name of the service
	 * @param redisSettings    > credentials
	 * @return a RedisService object ready to work
	 */
	public RedisService createService(String name, RedisSettings redisSettings)
	{
		return new RedisService(name, redisSettings);
	}
	
}
