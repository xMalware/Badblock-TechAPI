package fr.badblock.api.common.tech.redis.methods.set;

import fr.badblock.api.common.tech.redis.RedisService;
import fr.badblock.api.common.tech.redis.methods.RedisMethod;
import fr.badblock.api.common.utils.data.Callback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import redis.clients.jedis.Jedis;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class RedisObjectSetMethod<T> extends RedisMethod
{

	private RedisService		redisService;
	private String				key;
	private T					value;
	private boolean				indented;
	private Callback<Boolean>	callback;

	@Override
	public void work(Jedis jedis) 
	{
		if (getRedisService() == null)
		{
			return;
		}
		if (getKey() == null)
		{
			return;
		}
		jedis.set(getKey(), getRedisService().getGson(isIndented()).toJson(getValue()));
		if (getCallback() != null) 
		{
			getCallback().done(true, null);
		}
	}

}
