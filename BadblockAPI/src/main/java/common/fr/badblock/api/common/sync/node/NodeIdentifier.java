package fr.badblock.api.common.sync.node;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NodeIdentifier
{

	private String	environment;
	private String	type;
	private String	fullName;
	
	/**
	 * Get the full identifier
	 * @return
	 */
	public String getFullIdentifier()
	{
		return environment + "-" + type + "_" + fullName;
	}
	
}
