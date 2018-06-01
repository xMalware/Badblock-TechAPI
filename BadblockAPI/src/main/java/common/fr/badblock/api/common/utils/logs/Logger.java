package fr.badblock.api.common.utils.logs;

public class Logger
{

	public void log(LogType logType, String message)
	{
		Log.log(logType, getLogMessage(message));
	}

	private String getLogMessage(String message)
	{
		return String.format("%s %2d", getLogPrefix(), message);
	}
	
	private String getLogPrefix()
	{
		return String.format("[%s]", getClass().getSimpleName());
	}
	
}
