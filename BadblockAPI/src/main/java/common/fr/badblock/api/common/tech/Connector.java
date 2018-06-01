package fr.badblock.api.common.tech;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Data;

@Data
public abstract class Connector<T extends Service>
{
	
	private GsonBuilder					gsonBuilder 	= new GsonBuilder();
	private Gson						gson			= getGsonBuilder().create();
	private Gson						exposedGson		= getGsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	private ConcurrentMap<String, T>	services		= new ConcurrentHashMap<>();
	
	/**
	 * Register a new service
	 * @param service		> Service
	 * @return service
	 */
	public T registerService(T service)
	{
		getServices().put(service.getName(), service);
		return service;
	}
	
	/**
	 * Unregister an existing service
	 * @param service		> Service
	 * @return service
	 */
	public T unregisterService(T service) 
	{
		getServices().remove(service.getName());
		return service;
	}
	
}
