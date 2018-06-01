package fr.badblock.api.common.tech.redis.methods.get;

import fr.badblock.api.common.tech.redis.methods.RedisMethod;
import fr.badblock.api.common.utils.data.Callback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import redis.clients.jedis.Jedis;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class RedisStringGetMethod extends RedisMethod
{
	
	private String				key;
	private Callback<String>	callback;
	
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
		getCallback().done(jedis.get(getKey()), null);
	}

}
