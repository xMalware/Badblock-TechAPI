package fr.badblock.api.common.tech.redis.methods.del;

import fr.badblock.api.common.tech.redis.methods.RedisMethod;
import fr.badblock.api.common.utils.data.Callback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import redis.clients.jedis.Jedis;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class RedisDeleteKeysMethod extends RedisMethod
{
	
	private Callback<Boolean>	callback;
	private String[]			keys;
	
	@Override
	public void work(Jedis jedis)
	{
		if (getKeys() == null)
		{
			return;
		}
		jedis.del(getKeys());
		if (getCallback() != null)
		{
			getCallback().done(true, null);
		}
	}

}
