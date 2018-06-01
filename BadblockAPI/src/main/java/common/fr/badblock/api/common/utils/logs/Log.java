package fr.badblock.api.common.utils.logs;

public class Log 
{

	//private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyy/MM/dd HH:mm:ss");

	public static String log(LogType logType, String message) 
	{
		//String date = simpleDateFormat.format(new Date());
		//String result = date + " " + logType.getChatColor() + "[" + logType.name() + "] " + ChatColor.RESET + message;
		System.out.println(message);
		return message;
	}

}
