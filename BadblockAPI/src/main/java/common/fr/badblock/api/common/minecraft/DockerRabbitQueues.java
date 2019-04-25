package fr.badblock.api.common.minecraft;

import lombok.Getter;

public enum DockerRabbitQueues
{

	KEEPALIVE("docker.keepalive"),
	SCLOSE("docker.sclose"),
	INSTANCE_KEEPALIVE("docker.instance.keepalive"),
	INSTANCE_STOP("docker.instance.stop"),
	INSTANCE_OPEN("docker.instance.open"),
	INSTANCE_CLOSE("docker.instance.close"),
	INSTANCE_PARTY_DATA("docker.instance.party_data"),
	PACKAGE_REQUEST("docker.package.request"),
	PACKAGE_UPDATEREQUEST("docker.package.updaterequest"),
	PACKAGE_REPLY("docker.package.reply"),
	
	LOG("docker.log");
	
	@Getter private String queue;
	
	DockerRabbitQueues(String queue)
	{
		this.queue = queue;
	}
	
}
