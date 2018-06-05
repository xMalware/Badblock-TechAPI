package fr.badblock.api.common.utils.data;

import java.lang.reflect.ParameterizedType;

/**
 * @author xMalware
 * @param <T>
 */
public abstract class DoubleCallback<T>
{

	public abstract T done();

	@SuppressWarnings("unchecked")
	public Class<T> getGenericPacketClass()
	{
		return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

}
