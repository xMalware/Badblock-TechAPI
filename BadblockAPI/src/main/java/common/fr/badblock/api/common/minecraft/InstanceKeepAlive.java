package fr.badblock.api.common.minecraft;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter public class InstanceKeepAlive {

	private String 			name;
	private boolean 		isJoinable;
	private GameState		gameState;
	private int					players;

	public InstanceKeepAlive(String name, boolean isJoinable, GameState gameState, int players)
	{
		this.setName(name);
		this.setJoinable(isJoinable);
		this.setGameState(gameState);
		this.setPlayers(players);
	}

}
