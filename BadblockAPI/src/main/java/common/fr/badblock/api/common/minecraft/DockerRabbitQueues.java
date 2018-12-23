package fr.badblock.api.common.minecraft;

import lombok.Getter;

public enum DockerRabbitQueues
{

	KEEPALIVE("docker.keepalive"),
	SCLOSE("docker.sclose"),
	INSTANCE_KEEPALIVE("docker.instance.keepalive"),
	INSTANCE_STOP("docker.instance.stop"),
	INSTANCE_OPEN("docker.instance.open"),
	
	LOG("docker.log");
	
	@Getter private String queue;
	
	DockerRabbitQueues(String queue)
	{
		this.queue = queue;
	}
	
}
