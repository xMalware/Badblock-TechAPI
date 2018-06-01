package fr.badblock.api.common.tech.redis.threading;

import fr.badblock.api.common.tech.TechThread;
import fr.badblock.api.common.tech.redis.RedisService;
import fr.badblock.api.common.tech.redis.methods.RedisMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class RedisThread extends TechThread<RedisMethod> 
{

	private RedisService redisService;

	public RedisThread(RedisService redisService, int id)
	{
		super("RedisThread", redisService.getQueue(), id);
		setRedisService(redisService);
	}

	@Override
	public void work(RedisMethod redisMethod) throws Exception
	{
		redisMethod.work(getRedisService());
	}

	@Override
	public String getErrorMessage()
	{
		return "[RedisConnector] An error occurred while trying to send packet.";
	}

	@Override
	public boolean isServiceAlive()
	{
		return getRedisService().isAlive();
	}

}
