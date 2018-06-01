package fr.badblock.api.common.tech.sql.thread;

import fr.badblock.api.common.tech.TechThread;
import fr.badblock.api.common.tech.sql.SQLService;
import fr.badblock.api.common.tech.sql.methods.SQLMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
public class SQLMethodThread extends TechThread<SQLMethod> 
{

	private SQLService sqlService;

	public SQLMethodThread(SQLService sqlService, int id)
	{
		super("SQLMethodThread", sqlService.getMethodQueue(), id);
		setSqlService(sqlService);
	}

	@Override
	public void work(SQLMethod sqlMethod) throws Exception
	{
		sqlMethod.run(getSqlService());
	}

	@Override
	public String getErrorMessage()
	{
		return "[SQLConnector] An error occurred while trying to send packet.";
	}

	@Override
	public boolean isServiceAlive()
	{
		return getSqlService().isAlive();
	}

}
