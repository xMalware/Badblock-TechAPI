package fr.badblock.api.common.utils.i18n;

import lombok.Data;

/**
 * Repr�sente une cha�ne de caract�re traductible. Utiliser � plusieurs endroit
 * dans l'API pour simplifier.
 * 
 * @author LeLanN
 */
@Data
public class TranslatableString
{
	
	private String 		key;
	private Object[]	objects;

	/**
	 * Cr�e une nouvelle cha�ne traduisible
	 * 
	 * @param key
	 *            La key
	 * @param objects
	 *            Les arguments
	 */
	public TranslatableString(String key, Object... objects)
	{
		this.key = key;
		this.objects = objects;
	}
	
	/**
	 * R�cup�re le message sur plusieurs lignes
	 * 
	 * @param locale
	 *            La langue
	 * @return Le message
	 */
	public String[] get(Locale locale) {
		return I18n.getInstance().get(locale, key, objects);
	}

	/**
	 * R�cup�re la premi�re ligne du message
	 * 
	 * @param locale
	 *            La langue
	 * @return La ligne
	 */
	public String getAsLine(Locale locale)
	{
		return I18n.getInstance().get(locale, key, objects)[0];
	}

}
