package fr.badblock.api.common.tech.sql.packet;

import java.sql.ResultSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public abstract class SQLPacket 
{
	
	private String			query;
	private SQLPacketType	type;
	private boolean			doNotCloseResultSet;
	
	public void done(ResultSet resultSet) 
	{
		
	}
	
	public boolean canCloseResultSet()
	{
		return !isDoNotCloseResultSet();
	}
	
}
