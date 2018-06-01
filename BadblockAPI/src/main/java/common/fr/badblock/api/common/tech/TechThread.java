package fr.badblock.api.common.tech;

import java.util.Queue;

import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class TechThread<T> extends Thread
{

	private Queue<T> queue;

	public TechThread(String technologyName, Queue<T> queue, int id)
	{
		super("ToengaCommon/" + technologyName + "/" + id);
		setQueue(queue);
		start();
	}

	@Override
	public void run() 
	{
		synchronized (this) 
		{
			while (isServiceAlive())
			{
				while (!queue.isEmpty())
				{
					try
					{
						work(queue.poll());
					}
					catch (Exception exception)
					{
						Log.log(LogType.ERROR, getErrorMessage());
						exception.printStackTrace();
					}
				}
				laze();
			}
		}
	}

	public abstract String  getErrorMessage();
	public abstract boolean isServiceAlive();
	public abstract void	work(T data) throws Exception;

	public boolean canHandlePacket()
	{
		return isAlive() && getState().equals(State.WAITING);
	}

	public void stirHimself()
	{
		synchronized (this) 
		{
			this.notify();
		}
	}

	public void laze()
	{
		synchronized (this) 
		{
			try 
			{
				this.wait();
			}
			catch (InterruptedException exception) 
			{
				exception.printStackTrace();
			}
		}
	}

}
