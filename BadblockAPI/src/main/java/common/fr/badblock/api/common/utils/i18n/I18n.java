package fr.badblock.api.common.utils.i18n;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.Setter;

public class I18n
{
	
	@Getter@Setter
	private static I18n	instance				= new I18n();
	
	private final static Locale		def 		= Locale.FRENCH_FRANCE;
	public final static char		SEPARATOR	= '.';
	
	private Map<Locale, Language> languages;

	public I18n()
	{

	}

	public void load(File folder)
	{
		if (!folder.exists())
		{
			folder.mkdirs();
		}

		languages = Maps.newConcurrentMap();
		System.out.println("[I18N] Looking for i18n languages.. (in " + folder.getAbsolutePath() + ")");

		for (File languageFolder : folder.listFiles())
		{
			if (languageFolder.isDirectory())
			{
				Locale locale = Locale.getLocale(languageFolder.getName());
				if (locale == null)
				{
					System.out.println("[I18N] Invalid language folder (" + languageFolder.getName() + ") : Unknown language");
				}
				else 
				{
					System.out.println("[I18N] Lecture i18n ... (" + locale + ")");

					languages.put(locale, new Language(locale, languageFolder));
				}
			}
		}

		Locale def = Locale.FRENCH_FRANCE;

		if (!languages.containsKey(def))
		{
			languages.put(def, new Language(def, new File(folder, def.getLocaleId())));
		}
	}

	public Collection<Locale> getConfiguratedLocales()
	{
		return Collections.unmodifiableCollection(languages.keySet());
	}
	
	public Language getLanguage(Locale locale)
	{
		return languages.get(locale);
	}

	public String[] get(String key, Object... args)
	{
		return get(def, key, args);
	}

	public String getWord(String key, boolean plural, WordDeterminant determinant)
	{
		return getWord(def, key, plural, determinant);
	}

	public String getWord(Locale locale, String key, boolean plural, WordDeterminant determinant)
	{
		if(locale == null)
		{
			locale = def;
		}

		Language language = languages.get(locale);

		if(language == null)
		{
			throw new RuntimeException("Trying to access to an unconfigurated language !");
		}

		return language.getWord(key, plural, determinant);
	}

	public String[] get(Locale locale, String key, Object... args)
	{
		if (locale == null)
		{
			locale = def;
		}

		Language language = languages.get(locale);

		if (language == null)
		{
			throw new RuntimeException("Trying to access to an unconfigurated language !");
		}

		return language.get(key, args);
	}
	
	public String replaceColors(String base)
	{
		return ChatColor.translateAlternateColorCodes('&', base);
	}

	public String[] replaceColors(String... base)
	{
		for (int i=0;i<base.length;i++)
		{
			base[i] = replaceColors(base[i]);
		}

		return base;
	}

	public List<String> replaceColors(List<String> base)
	{
		List<String> result = new ArrayList<>();

		for (String line : base)
		{
			result.add(replaceColors(line));
		}

		return result;
	}

	public void save()
	{
		for (Language language : languages.values())
		{
			language.save();
		}
	}
	
	public static Locale getDefaultLocale()
	{
		return def;
	}

}
