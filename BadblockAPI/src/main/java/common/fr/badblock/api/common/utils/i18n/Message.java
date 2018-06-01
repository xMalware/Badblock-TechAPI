package fr.badblock.api.common.utils.i18n;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

public class Message
{
	public transient File file;

	private boolean useHeader,
	useShortHeader,
	useFooter;

	private String[][] messages;

	@Getter @Setter private boolean unknown;

	public Message()
	{

	}

	public Message(String whenUnknow)
	{
		this.useHeader 		= false;
		this.useShortHeader = false;
		this.useFooter		= false;

		verify(whenUnknow);
	}

	public void verify(String whenUnknow)
	{
		if(messages == null || messages.length == 0)
		{
			unknown = true;
			messages = new String[][]
					{
				new String[]
						{
								whenUnknow
						}
					};
		}
	}

	public boolean useHeader()
	{
		return useHeader;
	}

	public boolean useShortHeader()
	{
		return useShortHeader;
	}

	public boolean useFooter()
	{
		return useFooter;
	}

	public boolean isRandomMessage()
	{
		return messages.length > 1;
	}

	public String[] getUnformattedMessage()
	{
		String[] message = messages[new Random().nextInt(messages.length)];
		return Arrays.copyOf(message, message.length);
	}

	public String[][] getAllMessages()
	{
		return messages;
	}

}
