package fr.badblock.api.common.tech.rabbitmq;

import fr.badblock.api.common.tech.Connector;
import fr.badblock.api.common.tech.rabbitmq.setting.RabbitSettings;
import lombok.Getter;

/**
 * Main class where all interactions with RabbitMQ technology should start by this, 
 * for creating different services, with credentials and by the way getting different 
 * services to apply some useful things, such as listen a queue, an exchange or build and send a queued message
 * @author xMalware
 */
public class RabbitConnector extends Connector<RabbitService>
{
	
	// Singleton instance of RabbitConnector
	@Getter private static RabbitConnector	instance = new RabbitConnector();
	
	/**
	 * Create settings and be back with a RabbitSettings object which is useful for some operations, like using it for different services
	 * @param hostnames   > hostnames, we highly recommend DNS
	 * @param port 		  > RabbitMQ Cluster port, 5762 by default
	 * @param username    > username of that account
	 * @param virtualHost > virtual host where the account will be connected to
	 * @param password	  > the password of that account
	 * @return a RabbitSettings object
	 */
	public RabbitSettings createSettings(String[] hostnames, int port, String username, String virtualHost, String password, boolean automaticRecovery, int connectionTimeout, int requestedHeartbeat, int workerThreads)
	{
		return new RabbitSettings(hostnames, port, username, virtualHost, password, automaticRecovery, connectionTimeout, requestedHeartbeat, workerThreads);
	}
	
}
