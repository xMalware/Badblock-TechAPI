package fr.badblock.api.common.tech.mongodb.setting;

import java.util.Random;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import fr.badblock.api.common.tech.Settings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A MongoCredentials object
 * @author xMalware
 *
 */
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class MongoSettings extends Settings
{

	private String[]				hostnames;
	private	int						port;
	private String					username;
	private	String					password;
	private String					database;
	private int						workerThreads;

	public MongoSettings()
	{
		
	}
	
	@Override
	public MongoClient toFactory() {
		try
		{
			String[] hostnames = getHostnames();
			int hostnameId = new Random().nextInt(hostnames.length);
			System.out.println("mongodb://" + getUsername() + ":" + getPassword() + "@" + hostnames[hostnameId] + ":" + getPort() + "/" + getDatabase());
			MongoClient mongo = new MongoClient(
					  new MongoClientURI("mongodb://" + getUsername() + ":" + getPassword() + "@" + hostnames[hostnameId] + ":" + getPort() + "/" + getDatabase()));
			return mongo;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
