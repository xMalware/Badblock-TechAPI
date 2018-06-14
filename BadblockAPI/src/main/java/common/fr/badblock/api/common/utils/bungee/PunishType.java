package fr.badblock.api.common.utils.bungee;

public enum PunishType {

	BAN,
	
	UNBAN,

	MUTE,
	
	UNMUTE,

	WARN,
	
	KICK;
	
	public static PunishType getFromString(String string)
	{
		for (PunishType punishType : values())
		{
			if (punishType.name().equalsIgnoreCase(string))
			{
				return punishType;
			}
		}
		return null;
	}

}