package fr.badblock.api.common.tech.mongodb.threading;

import fr.badblock.api.common.tech.TechThread;
import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.methods.MongoMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class MongoThread extends TechThread<MongoMethod> 
{

	private MongoService mongoService;

	public MongoThread(MongoService mongoService, int id)
	{
		super("MongoThread", mongoService.getQueue(), id);
		setMongoService(mongoService);
	}

	@Override
	public void work(MongoMethod mongoMethod) throws Exception
	{
		mongoMethod.run(getMongoService());
	}

	@Override
	public String getErrorMessage()
	{
		return "[MongoConnector] An error occurred while trying to send packet.";
	}

	@Override
	public boolean isServiceAlive()
	{
		return getMongoService().isAlive();
	}

}
