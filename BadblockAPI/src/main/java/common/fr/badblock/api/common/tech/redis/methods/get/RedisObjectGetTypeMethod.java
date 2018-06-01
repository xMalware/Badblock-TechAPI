package fr.badblock.api.common.tech.redis.methods.get;

import java.lang.reflect.Type;

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
public class RedisObjectGetTypeMethod<T> extends RedisMethod
{

	private RedisService		redisService;
	private String				key;
	private Type				type;
	private boolean				indented;
	private Callback<T>			callback;
	
	@Override
	public void work(Jedis jedis)
	{
		if (getKey() == null)
		{
			return;
		}
		if (getCallback() == null)
		{
			return;
		}
		getCallback().done(getRedisService().getGson(isIndented()).fromJson(jedis.get(getKey()), getType()), null);
	}

}
