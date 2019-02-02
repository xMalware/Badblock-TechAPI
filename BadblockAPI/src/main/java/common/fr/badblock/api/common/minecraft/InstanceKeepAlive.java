package fr.badblock.api.common.minecraft;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class InstanceKeepAlive {

	private String 			name;
	private boolean 		isJoinable;
	private GameState		gameState;
	private int					players;
	private int					slots;
	private long				keepAliveTime;

	public InstanceKeepAlive(String name, boolean isJoinable, GameState gameState, int players, int slots)
	{
		this.setName(name);
		this.setJoinable(isJoinable);
		this.setGameState(gameState);
		this.setPlayers(players);
		this.setSlots(slots);
	}
	
	public void keepAlive()
	{
		this.keepAliveTime = System.currentTimeMillis() + 30_000L;
	}

}
