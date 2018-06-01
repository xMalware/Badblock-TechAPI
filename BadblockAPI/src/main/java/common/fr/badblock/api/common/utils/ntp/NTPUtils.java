package fr.badblock.api.common.utils.ntp;

import java.net.InetAddress;
import java.util.TimerTask;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import fr.badblock.api.common.utils.threading.TimerUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Test class for timestamp, useful for packets with TTL restrictions and synchronized time on different servers.
 * @author Aurelian
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class NTPUtils extends TimerTask 
{

	@Getter private static NTPUtils instance 		= new NTPUtils("ntp.ovh.net");
	private static long 			timeDiff	 	= -1;

	private String		ntpServer;

	public NTPUtils(String ntpServer) 
	{
		this.setNtpServer(ntpServer);
		System.out.println("Added NTP utils");
		TimerUtils.getTimer().schedule(this, 0, 30_000L);
	}

	@Override
	public void run() 
	{
		System.out.println("NTP timestamp");
		int tries = 0;
		boolean done = false;
		while (!done && tries < 5)
		{
			try 
			{
				long start = System.currentTimeMillis();
				System.out.println("NTP timestamp A");
				NTPUDPClient timeClient = new NTPUDPClient();
				timeClient.setDefaultTimeout(2);
				System.out.println("NTP timestamp B");
				InetAddress inetAddress = InetAddress.getByName(getNtpServer());
				System.out.println("NTP timestamp C");
				TimeInfo timeInfo = timeClient.getTime(inetAddress);
				System.out.println("NTP timestamp D");
				long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
				System.out.println("NTP timestamp E");
				long end = System.currentTimeMillis();
				System.out.println("NTP timestamp F");
				long diff = end - start;
				returnTime -= diff;
				timeDiff = end - returnTime;
				System.out.println("NTP timestamp G");
				done = true;
			}catch(Exception error)
			{
				Log.log(LogType.ERROR, "Error occurred while trying to fetch NTP time.");
			}
			tries++;
			try {
				Thread.sleep(3000);
			} catch (InterruptedException error) {
				error.printStackTrace();
			}
		}
	}

	public static long getTime() 
	{
		return System.currentTimeMillis() - timeDiff;
	}

}
