package fr.badblock.api.common.minecraft.party;

/**
 * 
 * Partyable
 * 
 * @author xMalware
 *
 */
public enum Partyable {

	/**
	 * With everyone
	 */
	WITH_EVERYONE,

	/**
	 * With nobody
	 */
	WITH_NOBODY,

	/**
	 * With only his friends
	 */
	WITH_ONLY_HIS_FRIENDS;

	/**
	 * Get the Partyable by string
	 * 
	 * @param string
	 * @return
	 */
	public static Partyable getByString(String string) {
		// For each partyable state
		for (Partyable partyable : values()) {
			// Check if it's the same
			if (string.equalsIgnoreCase(partyable.name())) {
				// Returns the partyable
				return partyable;
			}
		}
		// Returns null
		return null;
	}

}
