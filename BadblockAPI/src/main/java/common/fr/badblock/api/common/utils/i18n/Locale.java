package fr.badblock.api.common.utils.i18n;

import lombok.Getter;

/**
 * Repr�sente les diff�rents langages support�s par BadBlock.
 * 
 * @author LelanN
 */
public enum Locale
{
	
	/**
	 * Repr�sente la langue fran�aise telle qu'elle est parl�e en France
	 */
	FRENCH_FRANCE("fr_FR"),
	/**
	 * Repr�sente la langue anglaise telle qu'elle est parl�e aux Etats-Unis
	 */
	ENGLISH_US("en_US");

	public static Locale getLocale(String localeId)
	{
		for (Locale locale : values())
		{
			if (locale.getLocaleId().equalsIgnoreCase(localeId))
			{
				return locale;
			}
		}
		return null;
	}

	@Getter
	private String localeId;

	private Locale(String localeId)
	{
		this.localeId = localeId;
	}
}
