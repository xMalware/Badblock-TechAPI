package fr.badblock.api.bukkit.tech;

import java.io.File;

import org.bukkit.plugin.Plugin;

import fr.badblock.api.common.tech.mongodb.MongoService;
import fr.badblock.api.common.tech.mongodb.setting.MongoSettings;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.setting.RabbitSettings;
import fr.badblock.api.common.utils.FileUtils;
import fr.badblock.api.common.utils.GsonUtils;
import lombok.Getter;

public class TechManager
{
	
	@Getter
	private static RabbitService	rabbitService;
	
	@Getter
	private static MongoService		mongoService;
	
	public TechManager(Plugin plugin) throws Exception
	{
		File folder = new File(plugin.getDataFolder(), "tech");
		
		if (!folder.exists())
		{
			folder.mkdirs();
			throw new Exception("No tech file found.");
		}
		
		File mongoFile = new File(folder, "mongodb.json");
		
		if (!mongoFile.exists())
		{
			throw new Exception("No MongoDB tech file found.");
		}
		
		String rawMongoSettings = FileUtils.readFile(mongoFile);
		MongoSettings mongoSettings = GsonUtils.getGson().fromJson(rawMongoSettings, MongoSettings.class);
		mongoService = new MongoService("default", mongoSettings);
		
		File rabbitFile = new File(folder, "rabbitmq.json");
		
		if (!rabbitFile.exists())
		{
			throw new Exception("No RabbitMQ tech file found.");
		}
		
		String rawRabbitSettings = FileUtils.readFile(rabbitFile);
		RabbitSettings rabbitSettings = GsonUtils.getGson().fromJson(rawRabbitSettings, RabbitSettings.class);
		rabbitService = new RabbitService("default", rabbitSettings);
	}
	
}
