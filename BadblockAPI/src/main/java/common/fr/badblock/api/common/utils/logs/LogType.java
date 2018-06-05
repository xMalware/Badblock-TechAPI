package fr.badblock.api.common.utils.logs;

import lombok.Getter;

@Getter
public enum LogType 
{

	SUCCESS	(LogChatColor.GREEN),
	INFO	(LogChatColor.CYAN),
	ERROR	(LogChatColor.RED),
	WARNING	(LogChatColor.YELLOW),
	DEBUG	(LogChatColor.PURPLE);

	private String chatColor;

	LogType(String chatColor) 
	{
		this.chatColor = chatColor;
	}

}
