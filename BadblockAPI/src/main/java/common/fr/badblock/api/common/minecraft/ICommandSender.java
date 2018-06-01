package fr.badblock.api.common.minecraft;

public interface ICommandSender
{
	public String getName();
	
	public void sendRawMessage(String message);
}
