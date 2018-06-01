package fr.badblock.api.common.utils.general;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.Getter;
import lombok.Setter;

public class GsonUtils
{

	@Getter@Setter
	private static Gson	gson		= new Gson();
	@Getter@Setter
	private static Gson	prettyGson	= new GsonBuilder().setPrettyPrinting().create();
	
	public static JsonElement toJsonElement(String string)
	{
		JsonParser jsonParser = new JsonParser();
		JsonElement element = jsonParser.parse(string);
		return element;
	}
	
}
