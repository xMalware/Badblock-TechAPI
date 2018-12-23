package fr.badblock.api.common.minecraft.matchmaking;

import lombok.Getter;

public enum MatchmakingQueues
{

	ENTER_REQUEST("matchmaking.enter_request"),
	LOG("matchmaking.log");
	
	@Getter private String queue;
	
	MatchmakingQueues(String queue)
	{
		this.queue = queue;
	}
	
}
