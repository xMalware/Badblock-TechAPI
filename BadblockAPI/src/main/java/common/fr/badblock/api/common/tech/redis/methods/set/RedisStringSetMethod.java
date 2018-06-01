package fr.badblock.api.common.tech.redis.methods.set;

import fr.badblock.api.common.tech.redis.methods.RedisMethod;
import fr.badblock.api.common.utils.data.Callback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import redis.clients.jedis.Jedis;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class RedisStringSetMethod extends RedisMethod
{
	
	private String				key;
	private String				value;
	private Callback<Boolean>	callback;
	
	@Override
	public void work(Jedis jedis)
	{
		if (getKey() == null)	
		{
			return;
		}
		jedis.set(getKey(), getValue());
		if (getCallback() != null) 
		{
			getCallback().done(true, null);
		}
	}

}
