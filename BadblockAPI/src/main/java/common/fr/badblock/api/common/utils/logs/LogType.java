package fr.badblock.api.common.utils.logs;

import lombok.Getter;

@Getter
public enum LogType 
{

	SUCCESS	(ChatColor.GREEN),
	ERROR	(ChatColor.RED),
	WARNING	(ChatColor.YELLOW),
	DEBUG	(ChatColor.DARK_PURPLE);

	private ChatColor chatColor;

	LogType(ChatColor chatColor) 
	{
		this.chatColor = chatColor;
	}

}
