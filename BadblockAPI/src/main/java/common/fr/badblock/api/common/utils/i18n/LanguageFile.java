package fr.badblock.api.common.utils.i18n;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.badblock.api.common.utils.general.JsonUtils;
import fr.badblock.api.common.utils.logs.LogChatColor;

public class LanguageFile
{
	
	private final File				file;
	private Map<String, Message> 	content;
	
	public LanguageFile(File file, String whenUnknow)
	{
		this.file    = file;
		this.content = Maps.newLinkedHashMap();
		
		JsonObject object = JsonUtils.loadObject(file);
		
		for(Entry<String, JsonElement> entry : object.entrySet())
		{
			if(entry.getValue().isJsonObject())
			{
				content.put(entry.getKey().toLowerCase(), JsonUtils.convert(entry.getValue(), Message.class));
				content.get(entry.getKey().toLowerCase()).verify(LogChatColor.RED + entry.getKey());
			}
		}
	}
	
	public Message getMessage(String key, String def)
	{
		key = key.toLowerCase();
		
		if(!content.containsKey(key))
		{
			content.put(key, new Message(def));
		}
		
		return content.get(key);
	}
	
	public String getName()
	{
		return file.getName().split("\\.")[0].toLowerCase();
	}
	
	public void save()
	{
		JsonUtils.save(file, content, true);
	}
}
