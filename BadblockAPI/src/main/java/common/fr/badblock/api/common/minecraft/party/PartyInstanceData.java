package fr.badblock.api.common.minecraft.party;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PartyInstanceData
{

	private String	serverName;
	private String	master;
	private String[] slaves;
	
}

