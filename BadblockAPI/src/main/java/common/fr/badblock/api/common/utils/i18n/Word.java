package fr.badblock.api.common.utils.i18n;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data@NoArgsConstructor
public class Word
{
	
	private String pluralUndefined   	 = "";
	private String pluralDefined   	 	 = "";
	private String pluralSimple   	 	 = "";

	private String singularUndefined   	 = "";
	private String singularDefined   	 = "";
	private String singularSimple   	 = "";

	public String get(boolean plural, WordDeterminant determinant)
	{
		switch(determinant)
		{
			case DEFINED: return plural ? pluralDefined : singularDefined;
			case UNDEFINED: return plural ? pluralUndefined : singularUndefined;
			case SIMPLE: return plural ? pluralSimple : singularSimple;
			default: return "";
		}
	}

}
