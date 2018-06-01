package fr.badblock.api.common.tech.sql.setting;

import java.util.Random;

import com.zaxxer.hikari.HikariConfig;

import fr.badblock.api.common.tech.Settings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A SQLSettings object
 * @author xMalware
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SQLSettings extends Settings
{

	private	String[]		jbdcUrl;
	private	int				port;
	private	String			database;
	private	String			username;
	private	String			password;
	private int				maxPoolSize;
	private long			maxLifetime;
	private long			idleTimeout;
	private long			leakDetectionThreshold;
	private long			connectionTimeout;
	private int				workerThreads;

	public HikariConfig toFactory()
	{
		HikariConfig hikariConfig = new HikariConfig();
		Random random = new Random();
		String url = getJbdcUrl()[random.nextInt(getJbdcUrl().length)];
		hikariConfig.setJdbcUrl(url);
		hikariConfig.setUsername(getUsername());
		hikariConfig.setPassword(getPassword());
		hikariConfig.setMaximumPoolSize(getMaxPoolSize());
		hikariConfig.setMaxLifetime(getMaxLifetime());
		hikariConfig.setIdleTimeout(getIdleTimeout());
		hikariConfig.setLeakDetectionThreshold(getLeakDetectionThreshold());
		hikariConfig.setConnectionTimeout(getConnectionTimeout());
		return hikariConfig;
	}

}
