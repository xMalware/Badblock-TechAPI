package fr.badblock.api.common.tech.redis.setting;

import java.util.Random;

import fr.badblock.api.common.tech.Settings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import redis.clients.jedis.Jedis;

/**
 * A RedisCredentials object
 * @author xMalware
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class RedisSettings extends Settings
{

	private	String[]		hostnames;
	private	int				port;
	private	String			password;
	private int				database;
	private int				workerThreads;
	
	public Jedis toFactory()
	{
		String[] hostnames = getHostnames();
		int hostnameId = new Random().nextInt(hostnames.length);
		Jedis jedis = new Jedis(hostnames[hostnameId], getPort());
		jedis.auth(getPassword());
		jedis.select(getDatabase());
		return jedis;
	}

}
