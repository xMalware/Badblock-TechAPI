package fr.badblock.api.common.tests;

import fr.badblock.api.common.tech.rabbitmq.RabbitConnector;
import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListener;
import fr.badblock.api.common.tech.rabbitmq.listener.RabbitListenerType;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.tech.rabbitmq.setting.RabbitSettings;
import fr.badblock.api.common.utils.logs.Log;
import fr.badblock.api.common.utils.logs.LogType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This class is a test class using RabbitConnector of ToengaCommon.
 * It's an example which can be used.
 * @author xMalware
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class RabbitTest
{

	private static String[] HOSTNAMES		= new String[] 
			{
					"localhost"
			};
	private static int		PORT			= 5672;
	private static String	USERNAME		= "root";
	private static String	VIRTUALHOST		= "rabbit";
	private static String	PASSWORD		= "defaultpassword";
	private static boolean	RECOVERY		= true;
	private static int		TIMEOUT			= 60_000;
	private static int		HEARTBEAT		= 60;
	private static int		WORKER_TESTS	= 32;

	public static void main(String[] args) 
	{
		new RabbitTest();	
	}

	// Object
	private RabbitService rabbitService;

	private RabbitTest()
	{
		// Connect
		connect();
		// Create Test listener
		createTestListener();
		// Send 10 packets
		sendTestPackets(10);
	}

	private void connect()
	{
		RabbitConnector rabbitConnector = RabbitConnector.getInstance();
		RabbitSettings rabbitSettings = rabbitConnector.createSettings(HOSTNAMES, PORT, USERNAME, VIRTUALHOST, PASSWORD, RECOVERY, TIMEOUT, HEARTBEAT, WORKER_TESTS);
		RabbitService rabbitService = rabbitConnector.registerService(new RabbitService("default", rabbitSettings));
		rabbitConnector.registerService(rabbitService);
		setRabbitService(rabbitService);
	}

	private void createTestListener()
	{
		getRabbitService().addListener(new RabbitListener(getRabbitService(), "test", RabbitListenerType.SUBSCRIBER, true)
		{
			@Override
			public void onPacketReceiving(String body)
			{
				Log.log(LogType.DEBUG, "Hello! (" + body + ")");
			}
		});
	}

	private void sendTestPackets(int max)
	{
		for (int i=0;i<max;i++)
		{
			getRabbitService().sendPacket(new RabbitPacket(new RabbitPacketMessage(10000, "Hi."), "test", true, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER));
		}
	}

}
