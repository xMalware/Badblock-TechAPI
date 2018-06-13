package fr.badblock.api.common.sync.node;

import fr.badblock.api.common.utils.TimeUtils;

public class NodeLocalSender extends Thread
{

	private long waitTime;

	public NodeLocalSender(int keepAliveTime)
	{
		super("node-localSender");
		waitTime = Math.max(1, keepAliveTime);
	}

	@Override
	public void run()
	{
		while (true)
		{
			NodeSyncAPI.getInstance().keepAlive();
			TimeUtils.sleepInSeconds(waitTime);
		}
	}

}
