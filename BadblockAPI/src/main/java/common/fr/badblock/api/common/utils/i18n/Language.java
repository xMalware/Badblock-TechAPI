package fr.badblock.api.common.utils.i18n;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import fr.badblock.api.common.utils.general.ArraysUtils;
import fr.badblock.api.common.utils.general.JsonUtils;
import fr.badblock.api.common.utils.general.StringUtils;
import lombok.Getter;

public class Language
{

	@Getter private final Locale 							locale;
	private final		  File	 							configFile;
	private final		  File	 							textFolder;
	private final		  File	 							wordFolder;

	private final		  Map<String, Message>		 	messages;
	private final		  Map<String, LanguageWordFile> wordFiles;
	private 		  	  LanguageConfig				config;

	public Language(Locale locale, File folder)
	{
		if (!folder.exists())
		{
			folder.mkdirs();
		}

		this.locale    = locale;
		this.messages  = Maps.newConcurrentMap();
		this.wordFiles = Maps.newConcurrentMap();

		configFile	   = new File(folder, "config.json");
		config		   = JsonUtils.load(configFile, LanguageConfig.class);

		textFolder = new File(folder, "texts");
		wordFolder = new File(folder, "words");

		if(config == null)
		{
			config = new LanguageConfig();
		}
		if (!textFolder.exists())
		{
			textFolder.mkdirs();
		}
		if (!wordFolder.exists())
		{
			wordFolder.mkdirs();
		}

		for (File file : wordFolder.listFiles())
		{
			if (!file.isDirectory())
			{
				LanguageWordFile language = new LanguageWordFile(file);
				wordFiles.put(language.getName(), language);
			}
		}
	}

	public String[] get(String key, Object... args)
	{
		return formatMessage(getMessage(key), args);
	}

	public String getWord(String key, boolean plural, WordDeterminant determinant)
	{
		return getWord(key).get(plural, determinant);
	}

	public Message getMessage(String key) 
	{
		if(key.isEmpty())
		{
			throw new IllegalArgumentException("Empty key given");
		}

		key = key.toLowerCase();

		if (messages.containsKey(key))
		{
			return messages.get(key);
		}

		String[] splitted     = key.split("\\.");

		File file = textFolder;

		for (int i=0;i<splitted.length-1;i++)
		{
			file = new File(file, splitted[i]);

			if(!file.exists())
			{
				file.mkdirs();
			}
		}

		file = new File(file, splitted[splitted.length - 1] + ".json");

		Message message = null;

		if (!file.exists())
		{
			message = new Message(key);
			JsonUtils.save(file, message, true);
		}
		else
		{
			message = JsonUtils.load(file, Message.class);
			message.verify(key);
		}

		message.file = file;
		messages.put(key, message);

		return message;
	}

	public Word getWord(String key)
	{
		String[] splitted     = key.split("\\.");
		LanguageWordFile file = getLanguageWordFile(splitted[0].toLowerCase());

		if (file == null)
		{
			file = new LanguageWordFile(new File(wordFolder, splitted[0].toLowerCase() + ".json"));
			wordFiles.put(splitted[0].toLowerCase(), file);
		}

		key = StringUtils.join(splitted, ".", 1);

		return file.getWord(key);
	}

	protected LanguageWordFile getLanguageWordFile(String file)
	{
		file = file.toLowerCase();

		if (!wordFiles.containsKey(file))
		{
			return wordFiles.put(file, new LanguageWordFile(new File(wordFolder, file + ".json")));
		}
		else
		{
			return wordFiles.get(file);
		}
	}

	public String[] formatMessage(Message message, Object... args)
	{
		String[]     base   = message.getUnformattedMessage();
		List<String> result = new ArrayList<>();

		if (message.useHeader())
		{
			result.addAll(Arrays.asList(getHeader()));
		}
		else if (message.useShortHeader())
		{
			base[0] = getShortHeader() + base[0];
		}

		result.addAll(Arrays.asList(base));

		if (message.useFooter())
		{
			result.add(getFooter());
		}

		String[] arrayResult = result.toArray(new String[0]);

		for(int i=args.length-1;i>=0;i--)
		{
			String toString = toString(args[i]);
			arrayResult = ArraysUtils.replace(arrayResult, "%" + i, toString);
		}

		return arrayResult;
	}

	private String toString(Object object)
	{
		if(object == null)
		{
			return "null";
		}
		else if(object instanceof TranslatableWord)
		{
			return ((TranslatableWord) object).getWord(getLocale());
		}
		else if(object instanceof TranslatableString)
		{
			TranslatableString str = ((TranslatableString) object);
			return get(str.getKey(), str.getObjects())[0];
		}
		else
		{
			return object.toString();
		}
	}

	public String[] getHeader()
	{
		return config.header;
	}

	public String getFooter()
	{
		return config.footer;
	}

	public String getShortHeader()
	{
		return config.shortHeader;
	}

	public void save()
	{
		JsonUtils.save(configFile, config, true);

		for (Message message : messages.values())
		{
			JsonUtils.save(message.file, message, true);
		}

		for (LanguageWordFile file : wordFiles.values())
		{
			file.save();
		}
	}
	
}
