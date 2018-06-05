package fr.badblock.api.common.nodesync;

import fr.badblock.api.common.tech.rabbitmq.RabbitService;
import fr.badblock.api.common.tech.rabbitmq.ToengaQueues;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacket;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketEncoder;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketMessage;
import fr.badblock.api.common.tech.rabbitmq.packet.RabbitPacketType;
import fr.badblock.api.common.utils.data.DoubleCallback;
import lombok.Data;
import lombok.Getter;

@Data
public class NodeSyncAPI
{

	@Getter private static NodeSyncAPI				instance;

	private NodeIdentifier							nodeIdentifier;
	private NodeData								localNode;
	private DoubleCallback<ToengaInstanceStatus>	callback;
	private NodeLocalSender							localSender;
	private long									keepAlive;
	private RabbitService 							rabbitService;
	
	public NodeSyncAPI(RabbitService rabbitService, NodeIdentifier nodeIdentifier, int keepAlive, DoubleCallback<ToengaInstanceStatus> callback)
	{
		instance = this;
		setNodeIdentifier(nodeIdentifier);
		setKeepAlive(keepAlive);
		setCallback(callback);
		setRabbitService(rabbitService);
		setLocalSender(new NodeLocalSender(keepAlive));
		getLocalSender().start();
	}

	public NodeData generateLocalData(ToengaInstanceStatus toengaInstanceStatus)
	{
		if (localNode == null)
		{
			localNode = new NodeData(getNodeIdentifier(), getKeepAlive(), toengaInstanceStatus);
		}
		else
		{
			localNode.update(getKeepAlive());
		}
		return localNode;
	}

	public void keepAlive()
	{
		keepAlive(callback.done());
	}
	
	public void keepAlive(ToengaInstanceStatus toengaInstanceStatus)
	{
		NodeData toengaNode = generateLocalData(toengaInstanceStatus);
		RabbitPacketMessage rabbitPacketMessage = new RabbitPacketMessage(-1, toengaNode.toJson());
		RabbitPacket rabbitPacket = new RabbitPacket(rabbitPacketMessage, ToengaQueues.NODE_SYNC, false, RabbitPacketEncoder.UTF8, RabbitPacketType.PUBLISHER);
		getRabbitService().sendPacket(rabbitPacket);
	}
	
	public static NodeSyncAPI init(RabbitService rabbitService, NodeIdentifier nodeIdentifier, int keepAlive, DoubleCallback<ToengaInstanceStatus> callback)
	{
		return new NodeSyncAPI(rabbitService, nodeIdentifier, keepAlive, callback);
	}

}
